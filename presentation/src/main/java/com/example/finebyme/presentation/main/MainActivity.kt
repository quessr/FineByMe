package com.example.finebyme.presentation.main

import android.app.AlertDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.finebyme.presentation.R
import com.example.finebyme.presentation.databinding.ActivityMainBinding
import com.example.finebyme.presentation.navigation.BottomNavigationBar
import com.example.finebyme.presentation.navigation.FbmNavGraph
import com.example.finebyme.presentation.photoList.PhotoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(binding.root)

        setContent {
            MainScreen()
        }

//        setupNavigation()
        handleOnBackPressed()

        val cursor = contentResolver.query(
            Uri.parse("content://com.fbm.contentprovider/favorite"),
            null,
            null,
            null
        )

        Log.d("MainActivity", "cursor ${cursor?.count}")

    }

//    private fun setupNavigation() {
//        navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
//        navController = navHostFragment.navController
//        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
//
//        navHostFragment.childFragmentManager.addOnBackStackChangedListener {
//            Log.d(
//                "fbm backstack",
//                navHostFragment.childFragmentManager.backStackEntryCount.toString()
//            )
//        }
//    }

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navHostFragment.childFragmentManager.backStackEntryCount == 0) {
                    Log.d("fbm backstack", "isBackStackEmpty")

                    showExitConfirmationDialog()
                } else {
                    Log.d(
                        "fbm backstack",
                        navHostFragment.childFragmentManager.backStackEntryCount.toString()
                    )
                    navController.popBackStack()
                }
            }
        })
    }

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
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val photoListViewModel: PhotoListViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        FbmNavGraph(
            navController = navController,
            photoListViewModel = photoListViewModel,
            modifier = Modifier.padding(padding)
        )
    }
}