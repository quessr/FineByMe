package com.example.finebyme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.finebyme.R
import com.example.finebyme.data.network.RetrofitInstance
import com.example.finebyme.databinding.ActivityMainBinding
import com.example.finebyme.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFragment(HomeFragment())

        RetrofitInstance.fetchRandomPhoto()
        RetrofitInstance.fetchSearchPhoto()
    }

    private fun setFragment( frag : Fragment ) {
        supportFragmentManager.commit {
            replace(R.id.frameLayout, frag)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }
}