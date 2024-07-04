main
├── AndroidManifest.xml
├── java
│   └── com
│       └── example
│           └── finebyme
│               ├── common
│               │   └── enums
│               │       └── State.kt
│               ├── data
│               │   ├── datasource
│               │   │   ├── UnSplashDataSource.kt
│               │   │   └── UserDataSource.kt
│               │   ├── db
│               │   │   ├── FavoritePhotosDatabase.kt
│               │   │   ├── Photo.kt
│               │   │   └── PhotoDao.kt
│               │   ├── model
│               │   │   ├── SearchPhotoResponse.kt
│               │   │   └── UnsplashPhoto.kt
│               │   ├── network
│               │   │   ├── RetrofitInstance.kt
│               │   │   └── RetrofitService.kt
│               │   └── repository
│               │       └── PhotoRepository.kt
│               ├── di
│               │   └── AppViewModelFactory.kt
│               ├── ui
│               │   ├── base
│               │   │   ├── BaseFragment.kt
│               │   │   └── BaseViewModel.kt
│               │   ├── favoriteList
│               │   │   ├── FavoriteListFragment.kt
│               │   │   └── FavoriteListViewModel.kt
│               │   ├── main
│               │   │   └── MainActivity.kt
│               │   ├── photoDetail
│               │   │   ├── PhotoDetailActivity.kt
│               │   │   └── PhotoDetailViewModel.kt
│               │   ├── photoList
│               │   │   ├── PhotoAdapter.kt
│               │   │   ├── PhotoListFragment.kt
│               │   │   └── PhotoListViewModel.kt
│               │   └── splash
│               │       └── SplashActivity.kt
│               └── utils
│                   ├── ImageLoader.kt
│                   ├── IntentUtils.kt
│                   ├── LoadingHandler.kt
│                   ├── NetworkUtils.kt
│                   └── PhotoDiffCallback.kt
