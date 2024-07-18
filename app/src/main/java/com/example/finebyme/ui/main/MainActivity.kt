package com.example.finebyme.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.finebyme.R
import com.example.finebyme.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController
    private var isBackStackEmpty = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupNavigation()
        handleOnBackPressed()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
//        binding.bottomNavigation.setupWithNavController(navController)

        navHostFragment.childFragmentManager.addOnBackStackChangedListener {
            isBackStackEmpty = if (navHostFragment.childFragmentManager.backStackEntryCount == 0) {
                Log.d("fbm backstack init", "fbm backstack init ${navHostFragment.childFragmentManager.backStackEntryCount.toString()}")
                true
            } else {
                Log.d("fbm backstack", navHostFragment.childFragmentManager.backStackEntryCount.toString())
                false
            }
        }
    }

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (isBackStackEmpty) {
                    Log.d("fbm backstack", "isBackStackEmpty")

                    showExitConfirmationDialog()
                } else {
                    navController.popBackStack()
                }
            }
        })
    }


//    private fun handleOnBackPressed() {
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//                if (supportFragmentManager.backStackEntryCount == 1) {
//                    showExitConfirmationDialog()
//                } else {
//                    supportFragmentManager.popBackStack()
//
//                    supportFragmentManager.addOnBackStackChangedListener(object :
//                        FragmentManager.OnBackStackChangedListener {
//                        override fun onBackStackChanged() {
//                            supportFragmentManager.removeOnBackStackChangedListener(this)
//
//                            val currentFragment =
//                                supportFragmentManager.findFragmentById(R.id.navHostFragment)
//
//                            Log.d("@@@@@@", "currentFragment : $currentFragment")
//                            when (currentFragment) {
//                                is PhotoListFragment -> {
//                                    binding.bottomNavigation.menu.findItem(R.id.nav_home).isChecked =
//                                        true
//                                }
//
//                                is FavoriteListFragment -> {
//                                    binding.bottomNavigation.menu.findItem(R.id.nav_favorite).isChecked =
//                                        true
//                                }
//                            }
//
//                        }
//
//                    })
//                }
//            }
//        })
//    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.dialog_exit_confirmation)
            .setPositiveButton(R.string.confirm) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

//    private fun initBottomNav() {
//        binding.bottomNavigation.itemIconTintList = null
//
//        binding.bottomNavigation.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.nav_home -> {
//                    changeFragment(PhotoListFragment::class.java.name)
//                    Log.d("BottomNav", "Home selected")
//                }
//
//                R.id.nav_favorite -> {
//                    changeFragment(FavoriteListFragment::class.java.name)
//                    Log.d("BottomNav", "Favorite selected")
//                }
//            }
//
//            true
//        }
//    }

//    private fun changeFragment(tag: String) {
//        // 현재 프래그먼트 찾기
//        var fragment = supportFragmentManager.findFragmentByTag(tag)
//
//        if (fragment == null) {
//            fragment = when (tag) {
//                PhotoListFragment::class.java.name -> PhotoListFragment()
//                FavoriteListFragment::class.java.name -> FavoriteListFragment()
//                else -> throw IllegalStateException("Unknown fragment tag: $tag")
//            }
//        }
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.navHostFragment, fragment, tag)
//            .setReorderingAllowed(true)
//            .addToBackStack(tag)
//            .commit()
//
//        Log.d("tag : ", tag)
//    }
//
//    private fun showInit() {
//        changeFragment(PhotoListFragment::class.java.name)
//    }

}