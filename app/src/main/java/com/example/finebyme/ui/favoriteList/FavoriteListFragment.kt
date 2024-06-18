package com.example.finebyme.ui.favoriteList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.repository.FavoritePhotosImpl
import com.example.finebyme.databinding.FragmentFavoriteListBinding
import com.example.finebyme.ui.photoList.PhotoAdapter
import com.example.finebyme.di.AppViewModelFactory
import com.example.finebyme.utils.IntentUtils.newPhotoDetail

class FavoriteListFragment : Fragment() {
    private lateinit var photoAdapter: PhotoAdapter
    private val favoriteListViewModel: FavoriteListViewModel by viewModels {
        val application = requireActivity().application
        val photoDao = FavoritePhotosDatabase.getDatabase(application).PhotoDao()
        val favoritePhotosRepository = FavoritePhotosImpl(photoDao)
        AppViewModelFactory(application, favoritePhotosRepository)
    }

    private var _binding: FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private var isPass = false

//    companion object {
//        private const val ARG_PHOTO = "photo"
//        fun newIntent(context: Context, photo: Photo): Intent {
//            return Intent(context, PhotoDetailActivity::class.java).apply {
//                putExtra(ARG_PHOTO, photo)
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoAdapter = PhotoAdapter(favoriteListViewModel)
        recyclerView = binding.recyclerView

        val numberOfColumns = 2
        val layoutManager =
            StaggeredGridLayoutManager(numberOfColumns, LinearLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView.layoutManager = layoutManager

        binding.recyclerView.adapter = photoAdapter

//        RetrofitInstance.fetchRandomPhoto { photos -> photos?.let { photoAdapter.setPhoto(it) } }

        favoriteListViewModel.photos.observe(
            viewLifecycleOwner
        ) { photos -> photoAdapter.setPhoto(photos) }

        photoAdapter.setOnPhotoClickListener(object : PhotoAdapter.OnPhotoClickListener {
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