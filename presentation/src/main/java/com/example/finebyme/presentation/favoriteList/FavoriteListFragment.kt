package com.example.finebyme.presentation.favoriteList

//@AndroidEntryPoint
//class FavoriteListFragment : Fragment() {
//
//    private val favoriteListViewModel: FavoriteListViewModel by activityViewModels()
//    private var isPass = false
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return ComposeView(requireContext()).apply {
//            setContent {
//                val photos by favoriteListViewModel.photos.observeAsState(emptyList())
//
//                PhotoStaggeredGrid(
//                    photos = photos,
//                    onPhotoClick = { photo ->
//                        val intent = newPhotoDetail(requireContext(), photo)
//                        startActivity(intent)
//                    }
//                )
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (isPass) {
//            isPass = false
//            return
//        }
//        favoriteListViewModel.onResumeScreen()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//    }
//}