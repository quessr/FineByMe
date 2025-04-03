package com.example.finebyme.presentation.photoList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.interaction.OnPhotoClickListener
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.databinding.FragmentPhotoListBinding
import com.example.finebyme.presentation.utils.IntentUtils.newPhotoDetail
import com.example.finebyme.presentation.utils.LoadingHandler
import com.example.finebyme.presentation.utils.SnackbarUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var loadingHandler: LoadingHandler<FragmentPhotoListBinding>

    private val photoListViewModel: PhotoListViewModel by activityViewModels()

    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoListBinding.inflate(inflater)
        loadingHandler = LoadingHandler(binding, requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupListeners()

        binding.composeSearchBar.setContent {
            Log.d("SearchBar", "setContent 호출됨")
            var searchText by rememberSaveable { mutableStateOf("") }
            val focusManager = LocalFocusManager.current

            SearchBar(
                searchText = searchText,
                onTextChange = {
                    searchText = it
                    photoListViewModel.searchPhotos(it)
                },
                onSearch = {
                    Log.d("Search", "Search submitted: $searchText")
                    focusManager.clearFocus()
                },
                onCancle = {
                    searchText = ""
                    photoListViewModel.searchPhotos("")
                    focusManager.clearFocus()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        photoAdapter.clearData()
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
            viewLifecycleOwner
        ) { photos ->
            // photoAdapter.submitList(photos.toPhotoList(requireContext()))
            photoAdapter.submitList(photos)
        }

        photoListViewModel.loadingState.observe(viewLifecycleOwner) { loadingState ->
            loadingHandler.setLoadingState(loadingState)
        }

        photoListViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Log.d("PhotoListFragment", "Received error message: $errorMessage")
            SnackbarUtils.showSnackbar(requireContext(), binding.root, errorMessage, true)
//            hideKeyboard()
        }
    }

    private fun setupListeners() {
        photoAdapter.setOnPhotoClickListener(object : OnPhotoClickListener {
            override fun onPhotoClick(photo: Photo) {
                val intent = newPhotoDetail(requireContext(), photo)
                startActivity(intent)
            }
        })

//        binding.editTextSearch.apply {
//            setOnEditorActionListener { _, actionId, _ ->
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    handleSearchAction()
//                    true
//                } else {
//                    false
//                }
//            }
//
//            // text input의 텍스트가 변경 될때마다 동작
//            addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    handleTextChange(p0)
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                }
//
//            })
//
//            setOnFocusChangeListener()
//            { _, hasFocus ->
//                handleFocusChange(hasFocus)
//            }
//        }
//
//        binding.tvCancleInput.setOnClickListener {
//            binding.tvCancleInput.isVisible = false
//            binding.editTextSearch.text.clear()
//            hideKeyboard()
//        }
//    }
//
//    /**검색어 입력후 엔터키를 눌렀을 때 동작*/
//    private fun handleSearchAction() {
//        val query = binding.editTextSearch.text.toString()
//        Log.d("@@@@@@", "Search query: $query")
////        /**텍스트 입력 후 엔터를 치면 search photos api 호출 방식*/
////        photoListViewModel.searchPhotos(query)
//
//        hideKeyboard()
//        binding.editTextSearch.clearFocus()
//        binding.headerLayout.requestFocus()
//        binding.tvCancleInput.isVisible = false
//    }
//
//    private fun handleTextChange(query: CharSequence?) {
//        /**텍스트가 변경될 때마다 photos api 호출 방식*/
//        photoListViewModel.searchPhotos(query.toString())
//        binding.tvCancleInput.isVisible = query?.isNotEmpty() == true
//
//        if (query.isNullOrEmpty()) {
//            hideKeyboard()
//            binding.editTextSearch.clearFocus()
//            binding.headerLayout.requestFocus()
//        }
//    }
//
//    private fun handleFocusChange(hasFocus: Boolean) {
//        if (hasFocus) {
//            binding.tvCancleInput.isVisible = true
//        } else {
//            binding.tvCancleInput.isVisible = binding.editTextSearch.text.isNotEmpty()
//        }
//    }
//
//
//    private fun hideKeyboard() {
//        val imm = ContextCompat.getSystemService(
//            requireContext(),
//            android.view.inputmethod.InputMethodManager::class.java
//        )
//        val view = requireActivity().currentFocus
//        view?.let {
//            imm?.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }
//

    }

    @Composable
    fun SearchBar(
        searchText: String,
        onTextChange: (String) -> Unit,
        onSearch: () -> Unit,
        onCancle: () -> Unit
    ) {
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }
        val isFocused by interactionSource.collectIsFocusedAsState()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .wrapContentHeight(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                singleLine = true,
                interactionSource = interactionSource,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearch()
                    focusManager.clearFocus()
                }),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color(0xFF464646),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(start = 8.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null,
                            tint = Color(0xC0C0C0C0),
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            if (searchText.isEmpty()) {
                                Text(
                                    "검색",
                                    color = Color(0xC0C0C0C0),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            if (searchText.isNotEmpty() || isFocused) {
                Text(
                    text = "취소",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onCancle() }
                )
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SearchBarPreview() {
        var text by remember { mutableStateOf("") }

        SearchBar(
            searchText = text,
            onTextChange = { text = it },
            onSearch = {},
            onCancle = { text = "" }
        )
    }
}

