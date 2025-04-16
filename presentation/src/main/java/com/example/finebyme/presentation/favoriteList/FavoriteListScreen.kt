package com.example.finebyme.presentation.favoriteList

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.presentation.common.component.PhotoStaggeredGrid

@Composable
fun FavoriteListScreen(
    viewModel: FavoriteListViewModel = hiltViewModel(),
    gridState: LazyStaggeredGridState,
    onPhotoClick: (Photo) -> Unit
) {
    val photos by viewModel.photos.observeAsState(emptyList())

    LaunchedEffect(Unit) { viewModel.onResumeScreen() }

    PhotoStaggeredGrid(
        photos = photos,
        onPhotoClick = onPhotoClick,
        gridState = gridState
    )
}