package com.example.finebyme.utils

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object NetworkUtils {
    fun <T> enqueueCall(call: Call<T>, onSuccess: (Response<T>) -> Unit, onFailure: (Throwable) -> Unit) {
        call.enqueue(object  : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    onSuccess(response)
                } else {
                    Log.e("NetworkUtils", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}