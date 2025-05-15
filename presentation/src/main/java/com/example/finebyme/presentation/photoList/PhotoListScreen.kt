package com.example.finebyme.presentation.photoList

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.common.component.Loading
import com.example.finebyme.presentation.common.component.PhotoStaggeredGrid
import com.example.finebyme.presentation.common.enums.LoadingState

@Composable
fun PhotoListScreen(
    viewModel: PhotoListViewModel,
    gridState: LazyStaggeredGridState,
    onPhotoCLick: (Photo) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val photos by viewModel.photos.collectAsState(emptyList())
    val photoItems = viewModel.photoPagingFlow.collectAsLazyPagingItems()
    val isLoading by viewModel.loadingState.collectAsState(initial = LoadingState.LOADING)
    val errorMessage by viewModel.errorMessage.collectAsState()
//    val snackbarHostState = remember { SnackbarHostState() }
    var searchText by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

//    LaunchedEffect(Unit) {
//        viewModel.onEnterScreen()
//    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 50.dp)
            ) { data ->
                val isError = true
                Snackbar(
                    snackbarData = data,
                    containerColor = if (isError) Color(0xFFD32F2F) else Color(0xFF323232),
                    contentColor = Color.White
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.Black)
        ) {
            SearchBar(
                searchText = searchText,
                onTextChange = {
                    searchText = it
                    viewModel.updateSearchQuery(it)
                },
                onSearch = {
                    Log.d("Search", "Search submitted: $searchText")
                    viewModel.updateSearchQuery(searchText)
                    focusManager.clearFocus()
                },
                onCancle = {
                    searchText = ""
                    viewModel.updateSearchQuery("")
                    focusManager.clearFocus()
                }
            )

            if (isLoading == LoadingState.LOADING) {
                Loading()
            } else {
                PhotoStaggeredGrid(
                    photos = photoItems,
                    onPhotoClick = onPhotoCLick,
                    gridState = gridState
                )
            }
        }
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