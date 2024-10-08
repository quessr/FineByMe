# Fine By Me

Fine By Me는 사용자가 즐겨찾기한 사진을 저장하고 관리할 수 있는 안드로이드 애플리케이션입니다. <br>이 앱은 Unsplash API를 통해 사진을 검색하고 즐겨찾기 및 다운로드할 수 있는 기능을 제공합니다.

## ✨ Features

- **사진 검색:** Unsplash API를 사용하여 원하는 사진을 검색할 수 있습니다.
- **사진 즐겨찾기:** 마음에 드는 사진을 즐겨찾기에 추가하여 쉽게 관리할 수 있습니다.
- **사진 다운로드:** 즐겨찾기한 사진을 기기에 다운로드할 수 있습니다.
- **즐겨찾기 관리:** 즐겨찾기한 사진을 삭제하거나 갤러리에서 확인할 수 있습니다.
- **핀터레스트 스타일 레이아웃:** 핀터레스트 스타일의 레이아웃을 적용하여 사진을 시각적으로 즐길 수 있습니다.
- **Content Provider:** Fine By Me는 Content Provider를 통해 favorite_photos_database를 외부 애플리케이션과 공유할 수 있습니다. 이를 통해 다른 앱에서도 즐겨찾기한 사진에 접근할 수 있습니다.

## 📸 Screenshots

<div style="display: flex; column-gap: 20px; overflow-x: auto">
  <img src="https://github.com/user-attachments/assets/655acc3e-10bc-455b-a752-6e7d079002c8" width="200"/>
  <img src="https://github.com/user-attachments/assets/872e26c7-0643-4738-98cd-eef35102285c" width="200"/>
  <img src="https://github.com/user-attachments/assets/d26abad7-afb4-4b18-af8d-b5a251e54c10" width="200"/>
  <img src="https://github.com/user-attachments/assets/dad2af30-26b2-40f5-be3f-909254220092" width="200"/>
</div> 


## 🛠 Skills

- **Kotlin:** 메인 프로그래밍 언어
- **Android Jetpack:** ViewModel, LiveData, Room, Data Binding, Navigation
- **Retrofit:** REST API 통신
- **Glide:** 이미지 로딩 및 캐싱
- **Hilt:** 의존성 주입
- **Coroutine:** 비동기 작업 처리
- **Content Provider:** 외부 애플리케이션과 데이터 공유를 위한 Android Content Provider 구현

## 🏛 Architecture
- **MVVM Pattern:** Model-View-ViewModel 아키텍처 패턴 사용
- **Repository Pattern:** 데이터 관리를 위한 Repository 패턴 사용
- **Clean Architecture:** 코드의 유지보수성과 확장성을 높이기 위한 Clean Architecture 아키텍처 패턴 사용

## 🏠 Project Structure

This document describes the structure of the project. For detailed information on each version, please refer to the links below.

- [PROJECT_STRUCTURE_MAIN](./docx/PROJECT_STRUCTURE_MAIN.md)
- [PROJECT_STRUCTURE_CLEAN_ARCHITECTURE](./docx/PROJECT_STRUCTURE_CLEAN_ARCHITECTURE.md)

<!-- ## 🛤 Roadmap

- **핀터레스트 레이아웃 라이브러리:** 프로젝트에서 사용한 핀터레스트 레이아웃을 라이브러리로 만들어 Jitpack에 배포할 예정입니다. -->

## 🔧 Environment Variables

To run this project, you will need to add the following environment variables to your local.properties file

`UNSPLASH_API_KEY`

## 🔗 Content Provider 설정

Fine By Me의 Content Provider를 사용하여 favorite_photos_database에 접근하기 위해서는 외부 앱의 AndroidManifest.xml 파일에 다음과 같은 설정을 추가해야 합니다.

1. 권한 설정:
```
<uses-permission android:name="com.fbm.contentprovider.READ_DATABASE" />
```
2. 쿼리 설정:
```
<queries>
    <package android:name="com.fbm.contentprovider" />
</queries>
```
이 설정을 통해, 다른 애플리케이션에서도 favorite_photos_database에 접근하여 즐겨찾기한 사진을 조회할 수 있습니다.

만약 `contentResolver`를 사용하여 Fine By Me의 즐겨찾기한 사진 정보를 가져오는 코드 예시가 필요하다면, [이 프로젝트](https://github.com/quessr/ContentProvider)의 코드를 참고하여 구현 방법을 확인할 수 있습니다.


## 🖼 Unsplash API

이 프로젝트는 [Unsplash API](https://unsplash.com/developers)를 사용하여 사진 검색 및 다운로드 기능을 구현합니다. <br>Unsplash API를 사용하기 위해서는 API 키가 필요하며, API 키는 Unsplash 개발자 포털에서 생성할 수 있습니다.