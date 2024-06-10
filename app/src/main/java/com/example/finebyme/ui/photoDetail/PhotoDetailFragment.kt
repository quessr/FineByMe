package com.example.finebyme.ui.photoDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.finebyme.R
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.network.RetrofitInstance
import com.example.finebyme.databinding.FragmentPhotoDetailBinding
import com.example.finebyme.databinding.FragmentPhotoListBinding
import com.example.finebyme.ui.photoList.PhotoAdapter

class PhotoDetailFragment : Fragment() {

    companion object {
        private const val ARG_PHOTO = "photo"
    }

    private var _binding: FragmentPhotoDetailBinding?= null
    private val binding get() = _binding!!
    private var photo: Photo? = null
    private lateinit var viewModel: PhotoDetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photo = it.getParcelable(ARG_PHOTO)
        }
        viewModel = ViewModelProvider(this).get(PhotoDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        // 미리 로드
//        photo?.let {
//            Glide.with(this)
//                .load(it.fullUrl)
//                .preload()
//        }

        photo.let {
            Glide.with(this)
                .load(it?.fullUrl)
                .centerCrop()
                .into(binding.ivPhoto)

            // 타이틀 변환
            val transformedTitle = it?.title?.let { title -> viewModel.transformTitle(title) }

            binding.tvTitle.text = transformedTitle
            binding.tvDescription.text = it?.description
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}