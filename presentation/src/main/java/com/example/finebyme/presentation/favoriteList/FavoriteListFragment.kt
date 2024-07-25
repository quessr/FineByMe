package com.example.finebyme.presentation.favoriteList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.interaction.OnPhotoClickListener
import com.example.finebyme.presentation.databinding.FragmentFavoriteListBinding
import com.example.finebyme.presentation.photoList.PhotoAdapter
import com.example.finebyme.presentation.utils.IntentUtils.newPhotoDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteListFragment : Fragment() {
    private lateinit var photoAdapter: PhotoAdapter

    private val favoriteListViewModel: FavoriteListViewModel by activityViewModels()

    private var _binding: FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private var isPass = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerview()
        setupObservers()
        setupListeners()

    }

    private fun setupRecyclerview() {
        photoAdapter = PhotoAdapter(favoriteListViewModel)
        recyclerView = binding.recyclerView

        val numberOfColumns = 2
        val layoutManager =
            StaggeredGridLayoutManager(numberOfColumns, LinearLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        recyclerView.layoutManager = layoutManager

        binding.recyclerView.adapter = photoAdapter
    }

    private fun setupObservers() {
        favoriteListViewModel.photos.observe(
            viewLifecycleOwner
        ) { photos ->
            val titles = photos.joinToString(", ") { it.title }
            Log.d("fmb Fragment", "Observed photo titles: $titles")
            photoAdapter.submitList(photos) {
                recyclerView.scrollToPosition(0)
            }
            binding.tvEmpty.isVisible = photos.isEmpty()
        }
    }

    private fun setupListeners() {
        photoAdapter.setOnPhotoClickListener(object : OnPhotoClickListener {
            override fun onPhotoClick(photo: Photo) {
                val intent = newPhotoDetail(requireContext(), photo)
                startActivity(intent)
            }
        })
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
        _binding = null
    }
}