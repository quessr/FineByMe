package com.example.finebyme.ui.photoList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.finebyme.R
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.repository.FavoritePhotosImpl
import com.example.finebyme.data.repository.FavoritePhotosRepository
import com.example.finebyme.databinding.FragmentPhotoListBinding

class PhotoListFragment : Fragment() {
    private lateinit var photoAdapter: PhotoAdapter

    //    private val photoListViewModel: PhotoListViewModel by viewModels()
    private val photoListViewModel: PhotoListViewModel by viewModels {
        val application = requireActivity().application
        val photoDao = FavoritePhotosDatabase.getDatabase(application).PhotoDao()
        val favoritePhotosRepository = FavoritePhotosImpl(photoDao)
        PhotoListViewModelFactory(application, favoritePhotosRepository)
    }

    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // db test를 위한 코드
//        photoListViewModel.setContext(requireContext())

        photoAdapter = PhotoAdapter()
        recyclerView = binding.recyclerView

        val numberOfColumns = 2
        val layoutManager =
            StaggeredGridLayoutManager(numberOfColumns, LinearLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView.layoutManager = layoutManager

        binding.recyclerView.adapter = photoAdapter

//        RetrofitInstance.fetchRandomPhoto{ photos -> photos?.let { photoAdapter.setPhoto(it) } }
        photoListViewModel.photos.observe(
            viewLifecycleOwner,
            Observer { photos -> photoAdapter.setPhoto(photos) })

        photoListViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                PhotoListViewModel.State.LOADING -> {
                    Glide.with(this).asGif()
                        .load(R.drawable.loading).into(binding.imageViewLoading)
                    binding.imageViewLoading.visibility = View.VISIBLE
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }

                PhotoListViewModel.State.DONE -> {
                    binding.imageViewLoading.visibility = View.GONE
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                }

                PhotoListViewModel.State.ERROR -> {
                    binding.imageViewLoading.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        photoAdapter.clearData()
    }
}