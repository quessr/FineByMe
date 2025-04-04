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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import coil.compose.AsyncImage
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.databinding.FragmentPhotoListBinding
import com.example.finebyme.presentation.utils.LoadingHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var loadingHandler: LoadingHandler<FragmentPhotoListBinding>

    private val photoListViewModel: PhotoListViewModel by activityViewModels()

//    private var _binding: FragmentPhotoListBinding? = null
//    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        _binding = FragmentPhotoListBinding.inflate(inflater)
//        loadingHandler = LoadingHandler(binding, requireContext())
//        return binding.root

        return ComposeView(requireContext()).apply {
            setContent {
                PhotoListScreen(photoListViewModel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerView()
        setupObservers()
//        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
        photoAdapter.clearData()
    }

//    private fun setupRecyclerView() {
//        photoAdapter = PhotoAdapter(photoListViewModel)
//        recyclerView = binding.recyclerView
//
//        val layoutManager =
//            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL).apply {
//                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
//            }
//        recyclerView.layoutManager = layoutManager
//        binding.recyclerView.adapter = photoAdapter
//    }

    private fun setupObservers() {
        photoListViewModel.photos.observe(
            viewLifecycleOwner
        ) { photos ->
            // photoAdapter.submitList(photos.toPhotoList(requireContext()))
//            photoAdapter.submitList(photos)
        }

        photoListViewModel.loadingState.observe(viewLifecycleOwner) { loadingState ->
            //TODO
//            loadingHandler.setLoadingState(loadingState)
        }

        photoListViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Log.d("PhotoListFragment", "Received error message: $errorMessage")
//            SnackbarUtils.showSnackbar(requireContext(), binding.root, errorMessage, true)
//            hideKeyboard()
        }
    }

//    private fun setupListeners() {
//        photoAdapter.setOnPhotoClickListener(object : OnPhotoClickListener {
//            override fun onPhotoClick(photo: Photo) {
//                val intent = newPhotoDetail(requireContext(), photo)
//                startActivity(intent)
//            }
//        })


    @Composable
    fun PhotoListScreen(viewModel: PhotoListViewModel) {
        var searchText by rememberSaveable { mutableStateOf("") }
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            val photos by viewModel.photos.observeAsState(emptyList())

            SearchBar(
                searchText = searchText,
                onTextChange = {
                    searchText = it
                    photoListViewModel.searchPhotos(it)
                },
                onSearch = {
                    Log.d("Search", "Search submitted: $searchText")
                    viewModel.searchPhotos(searchText)
                    focusManager.clearFocus()
                },
                onCancle = {
                    searchText = ""
                    photoListViewModel.searchPhotos("")
                    focusManager.clearFocus()
                }
            )

            PhotoListScreenContent(photos = photos, onPhotoCLick = {})
        }
    }

    @Composable
    fun PhotoListScreenContent(
        photos: List<Photo>,
        onPhotoCLick: (Photo) -> Unit = {}
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            content = {
                items(photos) { photo ->
                    PhotoItem(photo = photo, onClick = { onPhotoCLick(photo) })
                }
            }
        )
    }

    @Composable
    fun PhotoItem(
        photo: Photo,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable { onClick() },
        ) {
            AsyncImage(
                model = photo.thumbUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
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

    @Preview(showBackground = true)
    @Composable
    fun PhotoListScreenPreview() {
        PhotoListScreenContent(
            photos = List(10) { index ->
                dummyPhoto.copy(id = index.toString())
            },
            onPhotoCLick = {}
        )
    }


    private val dummyPhoto = Photo(
        id = "dummy123",
        title = "test",
        description = "test",
        thumbUrl = "",
        fullUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQE0crYeA-DTedLWvvdp0rHG0vf4BAVioYC0g&s"
    )
}





