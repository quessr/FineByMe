package com.example.finebyme.presentation.photoDetail

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.entity.calculateHeight
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.common.component.Loading
import com.example.finebyme.presentation.utils.PermissionDialogUtils
import com.example.finebyme.presentation.utils.PermissionUtils

@Composable
fun PhotoDetailScreen(
    viewModel: PhotoDetailViewModel = hiltViewModel(),
    photo: Photo,
    snackbarHostState: SnackbarHostState
) {
    val transformedPhoto by viewModel.transformedPhoto.observeAsState()
    val isFavorite by viewModel.isFavorite.observeAsState(initial = false)
    val isDownlaoding by viewModel.isDownloading.observeAsState(initial = false)
    val downloadState by viewModel.downloadState.observeAsState()
    val context = LocalContext.current
    val activity = context as? Activity

//    val snackbarHostState = remember { SnackbarHostState() }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidthPx = with(LocalDensity.current) { screenWidth.toPx() }.toInt()

    LaunchedEffect(photo.id) {
        viewModel.onEntryScreen(photo)
    }

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
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background((Color(0xFF2F2D2D)))
            ) {
                PhotoImage(
                    photo = photo,
                    itemWidth = itemWidthPx,
                    isFavorite = isFavorite,
                    isDownloading = isDownlaoding,
                    onFavoriteClick = { viewModel.toggleFavorite(photo) },
                    onDownloadClick = {
                        activity?.let {
                            PermissionUtils.checkAndRequestImagePermission(
                                activity = it,
                                onGranted = { viewModel.downloadImage(photo) },
                                onDenied = {
                                    PermissionDialogUtils.showPermissionDeniedDialog(
                                        context
                                    )
                                }
                            )
                        }
                    },
                )

                PhotoDetailTextSection(
                    photoTitle = transformed?.title ?: "제목이 없습니다.",
                    photoDescription = transformed?.description ?: "상세 설명이 없습니다."
                )
            }
        }
    }
}

@Composable
fun PhotoImage(
    photo: Photo,
    itemWidth: Int,
    onDownloadClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
    isDownloading: Boolean
) {
    val painter = rememberAsyncImagePainter(model = photo.thumbUrl)
    val state = painter.state

    val heightPx = photo.calculateHeight(itemWidth)
    val heightDp = with(LocalDensity.current) { heightPx.toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightDp)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
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



