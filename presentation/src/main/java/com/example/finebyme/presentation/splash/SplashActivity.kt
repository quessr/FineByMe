package com.example.finebyme.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.databinding.ActivitySplashBinding
import com.example.finebyme.presentation.utils.ImageLoader

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadGifImage()
        navigateToMainActivity()
    }

    private fun loadGifImage() {

        ImageLoader.loadGif(
            context = this,
            resourceId = R.drawable.camera,
            circleCrop = true,
            imageView = binding.imageViewSplash
        )
        binding.imageViewSplash.visibility = View.VISIBLE
    }

    private fun navigateToMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, com.example.finebyme.presentation.main.MainActivity::class.java)
            startActivity(intent)

            finish()
        }, 3000)
    }
}