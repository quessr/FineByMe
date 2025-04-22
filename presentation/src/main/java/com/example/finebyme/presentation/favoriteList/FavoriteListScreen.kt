package com.example.finebyme.presentation.favoriteList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.common.component.PhotoStaggeredGrid

@Composable
fun FavoriteListScreen(
    viewModel: FavoriteListViewModel = hiltViewModel(),
    gridState: LazyStaggeredGridState,
    onPhotoClick: (Photo) -> Unit
) {
    val photos by viewModel.photos.observeAsState(emptyList())

    LaunchedEffect(Unit) { viewModel.onResumeScreen() }

    if (photos.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.empty_favorites_message),
                color = Color(0x66000000),
                fontSize = 20.sp
            )
        }
    } else {
        PhotoStaggeredGrid(
            photos = photos,
            onPhotoClick = onPhotoClick,
            gridState = gridState
        )
    }
}