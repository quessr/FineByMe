package com.example.finebyme.data.network

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.unsplash.com/photos/"

    private const val CLIENT_ID_HEADER = "Authorization"
    private const val CLIENT_ID = "UNSPLASH_API_KEY"


    // Retrofit 인스턴스 생성
    private val retrofit: Retrofit by lazy {
        /** Retrofit by lazy :
         * Retrofit과 Kotlin의 by lazy 기능을 사용하여 Retrofit 객체와 서비스 객체를 생성 하는 방법.
         * 이는 애플리케이션이 시작될 때 즉시 생성되는 것이 아니라 처음 필요할 때 생성되게 한다.
         * 이렇게 하면 애플리케이션이 시작될 때 불필요한 로딩 프로세스가 방지되고 필요한 경우에만 데이터 검색이 발생한다.*/

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest: Request = chain.request()
                val requestWithHeader: Request = originalRequest.newBuilder()
                    .header(CLIENT_ID_HEADER, CLIENT_ID)
                    .build()
                chain.proceed(requestWithHeader)
            }
            .build()

        // Retrofit 빌더를 사용하여 Retrofit 인스턴스 생성
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}


