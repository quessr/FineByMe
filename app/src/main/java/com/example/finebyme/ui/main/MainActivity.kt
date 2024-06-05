package com.example.finebyme.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.finebyme.R
import com.example.finebyme.databinding.ActivityMainBinding
import com.example.finebyme.ui.favoriteList.FavoriteListFragment
import com.example.finebyme.ui.photoDetail.PhotoDetailFragment
import com.example.finebyme.ui.photoList.PhotoListFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showInit()
        initBottomNav()
    }

    private fun initBottomNav() {
        binding.bottomNavigation.itemIconTintList = null

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    PhotoListFragment().changeFragment()
                    Log.d("BottomNav", "Home selected")
                }

                R.id.nav_favorite -> {
                    FavoriteListFragment().changeFragment()
                    Log.d("BottomNav", "Favorite selected")
                }
            }

            true
        }
    }

    private fun Fragment.changeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, this)
            .commit()
    }

    fun showInit() {
        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, PhotoListFragment())
        transaction.commit()
    }

}