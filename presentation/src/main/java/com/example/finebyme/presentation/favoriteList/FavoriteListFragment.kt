package com.example.finebyme.presentation.favoriteList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.finebyme.presentation.base.BaseViewModel
import com.example.finebyme.presentation.common.component.PhotoStaggeredGrid
import com.example.finebyme.presentation.utils.IntentUtils.newPhotoDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteListFragment : Fragment() {

    private val favoriteListViewModel: FavoriteListViewModel by activityViewModels()
    private var isPass = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val photos by favoriteListViewModel.photos.observeAsState(emptyList())
                PhotoStaggeredGrid(
                    photos = photos,
                    viewModel = BaseViewModel(),
                    onPhotoClick = { photo ->
                        val intent = newPhotoDetail(requireContext(), photo)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPass) {
            isPass = false
            return
        }
        favoriteListViewModel.onResumeScreen()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}