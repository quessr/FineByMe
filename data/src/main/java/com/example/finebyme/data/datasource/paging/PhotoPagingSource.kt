package com.example.finebyme.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.finebyme.data.datasource.UnSplashDataSource
import com.example.finebyme.data.mapper.PhotoMapper
import com.example.finebyme.domain.entity.Photo

class PhotoPagingSource(
    private val unSplashDataSource: UnSplashDataSource,
    private val query: String
) : PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 1
        val perPage = params.loadSize

        return try {
            val result = if (query.isBlank()) {
                unSplashDataSource.getRandomPhotoList(page, perPage)
            } else {
                unSplashDataSource.getSearchPhotoList(query, page, perPage)
            }

            val photos = PhotoMapper.mapToPhotoList(result)
            LoadResult.Page(
                data = photos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (photos.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? = null
}