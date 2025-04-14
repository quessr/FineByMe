package com.example.finebyme.presentation.favoriteList

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.finebyme.presentation.base.BaseViewModel
import com.example.finebyme.presentation.common.component.PhotoGrid
import com.example.finebyme.presentation.common.component.PhotoStaggeredGrid
import com.example.finebyme.presentation.databinding.FragmentFavoriteListBinding
import com.example.finebyme.presentation.photoList.PhotoAdapter
import com.example.finebyme.presentation.utils.IntentUtils.newPhotoDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteListFragment : Fragment() {

    //    private lateinit var photoAdapter: PhotoAdapter
    private val favoriteListViewModel: FavoriteListViewModel by activityViewModels()
    private var recyclerViewState: Parcelable? = null
    private var _binding: FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private var isPass = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        _binding = FragmentFavoriteListBinding.inflate(inflater)
//        return binding.root
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setupRecyclerview()
//        setupObservers()
//        setupListeners()

    }

    //TODO StaggeredGridLayout
//    private fun setupRecyclerview() {
//        photoAdapter = PhotoAdapter(favoriteListViewModel)
//        recyclerView = binding.recyclerView
//
//        val numberOfColumns = 2
//        val layoutManager =
//            StaggeredGridLayoutManager(numberOfColumns, LinearLayoutManager.VERTICAL).apply {
//                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
//            }
//
//        recyclerView.layoutManager = layoutManager
//        binding.recyclerView.adapter = photoAdapter
//    }

//    private fun setupObservers() {
//        favoriteListViewModel.photos.observe(
//            viewLifecycleOwner
//        ) { photos ->
//            photoAdapter.submitList(photos) {
//                if (recyclerViewState != null) {
//                    recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
//                } else {
//                    recyclerView.scrollToPosition(0)
//                }
//            }
//            binding.tvEmpty.isVisible = photos.isEmpty()
//        }
//    }

//    private fun setupListeners() {
//        photoAdapter.setOnPhotoClickListener(object : OnPhotoClickListener {
//            override fun onPhotoClick(photo: Photo) {
//                val intent = newPhotoDetail(requireContext(), photo)
//                startActivity(intent)
//            }
//        })
//    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
//        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    override fun onResume() {
        super.onResume()
        if (isPass) {
            isPass = false
            return
        }
        favoriteListViewModel.onResumeScreen()
    }

    override fun onPause() {
        super.onPause()
//        recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}