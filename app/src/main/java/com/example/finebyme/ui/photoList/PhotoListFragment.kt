package com.example.finebyme.ui.photoList

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.finebyme.R
import com.example.finebyme.common.enums.State
import com.example.finebyme.data.db.FavoritePhotosDatabase
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.model.toPhotoList
import com.example.finebyme.data.network.RetrofitInstance
import com.example.finebyme.data.network.RetrofitService
import com.example.finebyme.data.repository.FavoritePhotosRepositoryImpl
import com.example.finebyme.data.repository.SearchPhotosRepository
import com.example.finebyme.data.repository.SearchPhotosRepositoryImpl
import com.example.finebyme.databinding.FragmentPhotoListBinding
import com.example.finebyme.di.AppViewModelFactory
import com.example.finebyme.utils.ImageLoader
import com.example.finebyme.utils.IntentUtils.newPhotoDetail

class PhotoListFragment : Fragment() {

    private lateinit var photoAdapter: PhotoAdapter

    //    private val photoListViewModel: PhotoListViewModel by viewModels()
    private val photoListViewModel: PhotoListViewModel by viewModels {
        val application = requireActivity().application
        val photoDao = FavoritePhotosDatabase.getDatabase(application).PhotoDao()
        val favoritePhotosRepository = FavoritePhotosRepositoryImpl(photoDao)
        val retrofitService = RetrofitInstance.retrofitService
        val searchPhotosRepository = SearchPhotosRepositoryImpl(retrofitService)
        AppViewModelFactory(application, favoritePhotosRepository, searchPhotosRepository)
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

        photoAdapter = PhotoAdapter(photoListViewModel)
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
            Observer { photos ->
//                photos.forEach {photo -> Log.d("@@@@@", "photo title : ${photo.title}")}
                photoAdapter.setPhoto(photos.toPhotoList())
            })

        photoListViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                State.LOADING -> {
                    ImageLoader.loadGif(
                        context = binding.imageViewLoading.context,
                        resourceId = R.drawable.loading,
                        imageView = binding.imageViewLoading
                    )

                    binding.imageViewLoading.visibility = View.VISIBLE
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }

                State.DONE -> {
                    binding.imageViewLoading.visibility = View.GONE
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                }

                State.ERROR -> {
                    binding.imageViewLoading.visibility = View.GONE
                }
            }
        })

        photoAdapter.setOnPhotoClickListener(object : PhotoAdapter.OnPhotoClickListener {
            override fun onPhotoClick(photo: Photo) {
                val intent = newPhotoDetail(requireContext(), photo)
                startActivity(intent)
            }
        })

        /**텍스트 입력 후 엔터를 치면 search photos api 호출 방식*/
        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.editTextSearch.text.toString()
                Log.d("@@@@@@", "Search query: $query")
//                photoListViewModel.searchPhotos(query)
                hideKeyboard()
                binding.tvCancleInput.isVisible = false
                true
            } else {
                false
            }
        }

        /**텍스트가 변경될 때마다 photos api 호출 방식*/
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = p0.toString()

                photoListViewModel.searchPhotos(query)

                binding.tvCancleInput.isVisible = query.isNotEmpty()

                if (query.isEmpty()) {
                    hideKeyboard()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.editTextSearch.setOnClickListener {
            if (binding.editTextSearch.text.isNotEmpty()) {
                binding.tvCancleInput.isVisible = true
            }
        }

        binding.editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                Log.d("!!!!!!", "hasFocus : $hasFocus")
                binding.tvCancleInput.isVisible = true
            } else {
                binding.tvCancleInput.isVisible = binding.editTextSearch.text.isNotEmpty()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = ContextCompat.getSystemService(
            requireContext(),
            android.view.inputmethod.InputMethodManager::class.java
        )
        val view = requireActivity().currentFocus
        view?.let {
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        photoAdapter.clearData()
    }
}