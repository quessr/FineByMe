package com.example.finebyme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finebyme.R
import com.example.finebyme.data.network.RetrofitInstance

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RetrofitInstance.fetchRandomPhoto()
        RetrofitInstance.fetchSearchPhoto()
    }
}