package com.example.finebyme.presentation.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.entity.calculateHeight

@Composable
fun PhotoStaggeredGrid(
    photos: List<Photo>,
    onPhotoClick: (Photo) -> Unit
) {
    val density = LocalDensity.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemSpacing = 4.dp * 3
    val itemWidthDp = (screenWidth - itemSpacing) / 2

    val itemWidthPx = with(density) { itemWidthDp.toPx() }.toInt()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalItemSpacing = 4.dp
    ) {
        itemsIndexed(photos) { _, photo ->
            val heightPx = photo.calculateHeight(itemWidthPx)

            PhotoItem(
                photo = photo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { heightPx.toDp() })
                    .clip(RoundedCornerShape(8.dp)),
                onClick = { onPhotoClick(photo) }
            )
        }
    }
}