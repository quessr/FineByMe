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
import com.example.finebyme.utils.LoadingHandler

class PhotoListFragment : Fragment() {

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var loadingHandler: LoadingHandler

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoListBinding.inflate(inflater)
        loadingHandler = LoadingHandler(binding, requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        photoAdapter = PhotoAdapter(photoListViewModel)
        recyclerView = binding.recyclerView

        val layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            }
        recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = photoAdapter
    }

    private fun setupObservers() {
        photoListViewModel.photos.observe(
            viewLifecycleOwner,
            Observer { photos ->
                photoAdapter.setPhoto(photos.toPhotoList())
            })

        photoListViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            loadingHandler.setLoadingState(state)
        })
    }

    private fun setupListeners() {
        photoAdapter.setOnPhotoClickListener(object : PhotoAdapter.OnPhotoClickListener {
            override fun onPhotoClick(photo: Photo) {
                val intent = newPhotoDetail(requireContext(), photo)
                startActivity(intent)
            }
        })

        binding.editTextSearch.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handleSearchAction()
                    true
                } else {
                    false
                }
            }

            // text input의 텍스트가 변경 될때마다 동작
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    handleTextChange(p0)
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

            setOnFocusChangeListener()
            { _, hasFocus ->
                handleFocusChange(hasFocus)
            }
        }

        binding.tvCancleInput.setOnClickListener {
            binding.editTextSearch.text.clear()
        }
    }

    /**검색어 입력후 엔터키를 눌렀을 때 동작*/
    private fun handleSearchAction() {
        val query = binding.editTextSearch.text.toString()
        Log.d("@@@@@@", "Search query: $query")
//                /**텍스트 입력 후 엔터를 치면 search photos api 호출 방식*/
//                photoListViewModel.searchPhotos(query)
        hideKeyboard()
        binding.editTextSearch.clearFocus()
        binding.headerLayout.requestFocus()
        binding.tvCancleInput.isVisible = false
    }

    private fun handleTextChange(query: CharSequence?) {
        /**텍스트가 변경될 때마다 photos api 호출 방식*/
        photoListViewModel.searchPhotos(query.toString())
        binding.tvCancleInput.isVisible = query?.isNotEmpty() == true

        if (query.isNullOrEmpty()) {
            hideKeyboard()
            binding.editTextSearch.clearFocus()
            binding.headerLayout.requestFocus()
        }
    }

    private fun handleFocusChange(hasFocus: Boolean) {
        if (hasFocus) {
            Log.d("!!!!!!", "hasFocus : $hasFocus")
            binding.tvCancleInput.isVisible = true
        } else {
            binding.tvCancleInput.isVisible = binding.editTextSearch.text.isNotEmpty()
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