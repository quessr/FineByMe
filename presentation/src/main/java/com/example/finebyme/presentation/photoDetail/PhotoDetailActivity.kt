package com.example.finebyme.presentation.photoDetail

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.common.component.Loading
import com.example.finebyme.presentation.databinding.ActivityPhotoDetailBinding
import com.google.android.material.snackbar.Snackbar
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

        setupObservers()
        // TODO: setupListener()
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
        when (requestCode) {
            100, 200 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    showSnackbar(R.string.permission_granted.toString())
                    startDownload()
                } else {
                    finish()
//                    showSnackbar(R.string.permission_granted.toString())
                }
            }
        }
    }

    private fun startDownload() {
        photo?.let { photoDetailViewModel.downloadImage(it) }
    }

    private fun setupObservers() {
        photoDetailViewModel.transformedPhoto.observe(this) { transformedPhoto ->
            setupPhotoDetails(transformedPhoto)
        }

//        photoDetailViewModel.downloadState.observe(this) { message ->
//            showSnackbar(message)
//        }
    }

    private fun setupPhotoDetails(photo: Photo) {
        photo.let {

//            binding.tvTitle.text = it.title
//            binding.tvDescription.text = it.description

            Log.d("@@@@@@", " photo id : ${photo.id}")
        }
    }

//    private fun showSnackbar(message: String) {
//        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
//        val snackbarView: View = snackbar.view
//
//        // 상단 중앙으로 이동
//        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
//        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
//        snackbarView.layoutParams = params
//
//        val color = ContextCompat.getColor(this, R.color.black_40)
//        snackbar.setBackgroundTint(color)
//
//        snackbar.show()
//    }

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish() // 기본 동작으로 MainActivity로 돌아가기
            }
        })
    }

    @Composable
    fun PhotoDetailScreen(viewModel: PhotoDetailViewModel, photo: Photo) {
        val isFavorite by viewModel.isFavorite.observeAsState(initial = false)

        PhotoImage(
            photoUrl = photo.thumbUrl,
            isFavorite = isFavorite,
            isDownloading = viewModel.isDownloading.observeAsState(false).value,
            onDownloadClick = { requestPermissionDownload() },
            onFavoriteClick = { viewModel.toggleFavorite(photo) }
        )
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

            if (!isDownloading) {
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
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(32.dp),
                    color = Color.White
                )
            }
        }
    }
}