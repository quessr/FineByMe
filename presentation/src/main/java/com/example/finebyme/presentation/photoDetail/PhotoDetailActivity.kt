package com.example.finebyme.presentation.photoDetail

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.common.component.Loading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailActivity : AppCompatActivity() {

    companion object {
        private const val ARG_PHOTO = "photo"
        private val IMAGE_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        private const val REQUEST_CODE_TIRAMISU = 200
        private const val REQUEST_CODE_LEGACY = 100
    }

    private var photo: Photo? = null

    private val photoDetailViewModel: PhotoDetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhotoDetailScreen(viewModel = photoDetailViewModel, photo = photo!!)
        }

        photo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARG_PHOTO, Photo::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ARG_PHOTO)
        }

        if (photo != null) {
            // ViewModel에 Photo 객체를 전달하여 photo title 데이터 변환
            photoDetailViewModel.onEntryScreen(photo!!)
        } else {
            finish()
        }

        handleOnBackPressed()

    }

    private fun requestPermissionDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermissionsAndStartMotion(arrayOf(IMAGE_PERMISSION), REQUEST_CODE_TIRAMISU)
        } else {
            checkPermissionsAndStartMotion(arrayOf(IMAGE_PERMISSION), REQUEST_CODE_LEGACY)
        }
    }

    private fun checkPermissionsAndStartMotion(permissions: Array<String>, requestCode: Int) {
        val permissionResults = permissions.map {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (permissionResults.all { it }) {
            startDownload()
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_TIRAMISU) {
            val granted =
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

            if (granted) {
                startDownload()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, IMAGE_PERMISSION)) {
                    showPermissionDeniedDialog()
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다")
            .setMessage("이미지 다운로드를 위해 저장소 접근 권한이 필요합니다. 설정에서 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun startDownload() {
        photo?.let { photoDetailViewModel.downloadImage(it) }
    }

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish() // 기본 동작으로 MainActivity로 돌아가기
            }
        })
    }

    @Composable
    fun PhotoDetailScreen(viewModel: PhotoDetailViewModel, photo: Photo) {
        val transformedPhoto by viewModel.transformedPhoto.observeAsState()
        val isFavorite by viewModel.isFavorite.observeAsState(initial = false)
        val isDownlaoding by viewModel.isDownloading.observeAsState(initial = false)
        val downloadState by viewModel.downloadState.observeAsState()

        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(downloadState) {
            downloadState?.let { message ->
                snackbarHostState.showSnackbar(
                    message = message,
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }
        }

        transformedPhoto.let { transformed ->
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background((Color(0xFF2F2D2D)))
                ) {
                    PhotoImage(
                        photoUrl = photo.thumbUrl,
                        isFavorite = isFavorite,
                        isDownloading = isDownlaoding,
                        onDownloadClick = { requestPermissionDownload() },
                        onFavoriteClick = { viewModel.toggleFavorite(photo) }
                    )

                    PhotoDetailTextSection(
                        photoTitle = transformed!!.title,
                        photoDescription = transformed.description ?: "상세 설명이 없습니다."
                    )
                }
            }
        }
    }

    @Composable
    fun PhotoImage(
        photoUrl: String,
        onDownloadClick: () -> Unit,
        onFavoriteClick: () -> Unit,
        isFavorite: Boolean,
        isDownloading: Boolean
    ) {
        val painter = rememberAsyncImagePainter(model = photoUrl)
        val state = painter.state

        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            if (state is AsyncImagePainter.State.Loading) {
                Loading()
            }

            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 72.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .size(52.dp)
                    .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isFavorite) R.drawable.ic_nav_favorite_selected
                        else R.drawable.ic_nav_favorite_normal
                    ),
                    contentDescription = "Favorite",
                    tint = Color.White
                )
            }

            AssistChip(
                onClick = onDownloadClick,
                label = { Text("다운로드", color = Color.White) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color.Black.copy(alpha = 0.4f)
                ),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_file_download),
                        contentDescription = "download",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.Gray)
            )

            if (isDownloading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(32.dp),
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun PhotoDetailTextSection(photoTitle: String, photoDescription: String) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = photoTitle,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 20.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = photoDescription,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
