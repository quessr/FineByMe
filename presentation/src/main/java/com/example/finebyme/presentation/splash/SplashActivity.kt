package com.example.finebyme.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.databinding.ActivitySplashBinding
import com.example.finebyme.presentation.main.MainActivity
import com.example.finebyme.presentation.utils.ImageLoader

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setContent {
            SplashScreen {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}