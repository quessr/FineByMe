package com.example.finebyme.presentation.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.presentation.favoriteList.FavoriteListScreen
import com.example.finebyme.presentation.favoriteList.FavoriteListViewModel
import com.example.finebyme.presentation.photoDetail.PhotoDetailScreen
import com.example.finebyme.presentation.photoList.PhotoListScreen
import com.example.finebyme.presentation.photoList.PhotoListViewModel
import dagger.hilt.android.EntryPointAccessors

object Routes {
    const val PHOTO_LIST = "photo_list"
    const val FAVORITE_LIST = "favorite_list"
    const val PHOTO_DETAIL = "photo_detail"
}

@Composable
fun FbmNavGraph(
    navController: NavHostController,
    photoListViewModel: PhotoListViewModel,
    photoGridState: LazyStaggeredGridState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val moshi = EntryPointAccessors.fromApplication(
        context,
        MoshiEntryPoint::class.java
    ).moshi()

    val photoAdapter = moshi.adapter(Photo::class.java)

    NavHost(
        navController = navController,
        startDestination = Routes.PHOTO_LIST
    ) {
        composable(Routes.PHOTO_LIST) {
            PhotoListScreen(
                viewModel = photoListViewModel,
                gridState = photoGridState,
                onPhotoCLick = { photo ->
                    val photoJson = Uri.encode(photoAdapter.toJson(photo))
                    navController.navigate("${Routes.PHOTO_DETAIL}/$photoJson")
                },
                snackbarHostState = snackbarHostState
            )
        }

        composable(Routes.FAVORITE_LIST) {
            FavoriteListScreen(
                gridState = photoGridState,
                onPhotoClick = { photo ->
                    val photoJson = Uri.encode(photoAdapter.toJson(photo))
                    navController.navigate("${Routes.PHOTO_DETAIL}/$photoJson")
                }
            )
        }

        composable("${Routes.PHOTO_DETAIL}/{photoJson}") { navBackStackEntry ->
            val encoded = navBackStackEntry.arguments?.getString("photoJson")
            val decoded = Uri.decode(encoded)
            val photo = photoAdapter.fromJson(decoded)
            Log.d("photoJson", "photo: $photo")

            if (photo == null) {
                Log.d("photoJson", "photo is null! Check adapter or JSON")
            }

            photo?.let {
                PhotoDetailScreen(photo = it, snackbarHostState = snackbarHostState)
            }
        }
    }
}