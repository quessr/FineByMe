```
├── app
│   ├── build.gradle.kts
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com
│                   └── example
│                       └── finebyme
│                           ├── FineByMeApplication.kt
│                           └── di
│                               └── AppModule.kt
├── data
│   ├── build.gradle.kts
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com
│                   └── example
│                       └── finebyme
│                           └── data
│                               ├── common
│                               │   └── enums
│                               │       └── ErrorType.kt
│                               ├── datasource
│                               │   ├── UnSplashDataSource.kt
│                               │   └── UserDataSource.kt
│                               ├── db
│                               │   ├── FavoritePhotosDatabase.kt
│                               │   ├── Photo.kt
│                               │   └── PhotoDao.kt
│                               ├── mapper
│                               │   └── PhotoMapper.kt
│                               ├── model
│                               │   ├── SearchPhotoResponse.kt
│                               │   └── UnsplashPhoto.kt
│                               ├── network
│                               │   ├── RetrofitInstance.kt
│                               │   └── RetrofitService.kt
│                               ├── repository
│                               │   ├── PhotoRepositoryFake.kt
│                               │   └── PhotoRepositoryImpl.kt
│                               └── utils
│                                   ├── ErrorHandler.kt
│                                   └── NetworkUtils.kt
├── domain
│   ├── build.gradle.kts
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           └── java
│               └── com
│                   └── example
│                       └── finebyme
│                           └── domain
│                               ├── entity
│                               │   └── Photo.kt
│                               ├── interaction
│                               │   └── onPhotoClickListener.kt
│                               ├── repositoryInterface
│                               │   └── PhotoRepository.kt
│                               └── usecase
│                                   ├── CheckFavoritePhotoUseCase.kt
│                                   ├── GetFavoritePhotoListUseCase.kt
│                                   ├── GetRandomPhotoListUseCase.kt
│                                   ├── GetSearchPhotoListUseCase.kt
│                                   └── SetFavoritePhotoUseCase.kt
├── presentation
│   ├── build.gradle.kts
│   └── src
│       └── main
│           └── java
│               └── com
│                   └── example
│                       └── finebyme
│                           └── presentation
│                               ├── base
│                               │   ├── BaseFragment.kt
│                               │   └── BaseViewModel.kt
│                               ├── common
│                               │   └── enums
│                               │       └── LoadingState.kt
│                               ├── favoriteList
│                               │   ├── FavoriteListFragment.kt
│                               │   └── FavoriteListViewModel.kt
│                               ├── main
│                               │   └── MainActivity.kt
│                               ├── photoDetail
│                               │   ├── PhotoDetailActivity.kt
│                               │   └── PhotoDetailViewModel.kt
│                               ├── photoList
│                               │   ├── PhotoAdapter.kt
│                               │   ├── PhotoListFragment.kt
│                               │   └── PhotoListViewModel.kt
│                               ├── splash
│                               │   └── SplashActivity.kt
│                               └── utils
│                                   ├── ImageLoader.kt
│                                   ├── IntentUtils.kt
│                                   ├── LoadingHandler.kt
│                                   └── SnackbarUtils.kt