package com.example.finebyme.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.finebyme.domain.entity.Photo

@Composable
fun PhotoGrid(
    photos: List<Photo>,
    onClick: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        content = {
            items(photos) { photo ->
                PhotoItem(photo = photo, onClick = { onClick(photo) })
            }
        }
    )
}