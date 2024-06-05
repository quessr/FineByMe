package com.example.finebyme.ui.favoriteList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.finebyme.R
import com.example.finebyme.data.network.RetrofitInstance
import com.example.finebyme.databinding.FragmentFavoriteListBinding
import com.example.finebyme.ui.photoList.PhotoAdapter
import java.lang.Appendable

class FavoriteListFragment : Fragment() {
    private lateinit var photoAdapter: PhotoAdapter
    private var _binding: FragmentFavoriteListBinding?= null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
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

        photoAdapter = PhotoAdapter()
        recyclerView = binding.recyclerView

        val numberOfColumns = 2
        val layoutManager = StaggeredGridLayoutManager(numberOfColumns, LinearLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView.layoutManager = layoutManager

        binding.recyclerView.adapter = photoAdapter

        RetrofitInstance.fetchRandomPhoto { photos -> photos?.let { photoAdapter.setPhoto(it) } }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}