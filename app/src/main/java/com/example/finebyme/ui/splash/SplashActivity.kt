package com.example.finebyme.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.finebyme.R
import com.example.finebyme.databinding.ActivityMainBinding
import com.example.finebyme.databinding.ActivitySplashBinding
import com.example.finebyme.databinding.FragmentPhotoListBinding
import com.example.finebyme.ui.main.MainActivity
import com.example.finebyme.utils.ImageLoader

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }, 3000)
    }
}