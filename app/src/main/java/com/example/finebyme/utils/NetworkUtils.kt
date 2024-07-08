package com.example.finebyme.utils

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


object NetworkUtils {
    fun <T> enqueueCall(call: Call<T>, onSuccess: (Response<T>) -> Unit, onFailure: (Throwable) -> Unit) {
        call.enqueue(object  : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    // HTTP 응답 코드가 2xx인 경우
                    onSuccess(response)
                }  else {
                    // HTTP 응답 코드가 4xx, 5xx인 경우
                    // 서버 응답이 성공적이지 않을 때 HttpException을 생성하여 onFailure 콜백을 호출합니다.
                    val httpException = HttpException(response)
                    Log.e("NetworkUtils", "Error: ${httpException.message()}")
                    onFailure(httpException)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                // 네트워크 문제 또는 요청 중 예외 발생
                onFailure(t)
            }
        })
    }
}