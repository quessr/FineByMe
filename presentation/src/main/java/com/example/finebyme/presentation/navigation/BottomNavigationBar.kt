package com.example.finebyme.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finebyme.presentation.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = currentRoute(navController)

    NavigationBar(
        containerColor = Color(0xB3000000),
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
            .border(
                width = 1.dp,
                color = Color(0xB3000000),
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
            )
    ) {
        NavigationBarItem(
            selected = currentRoute == Routes.PHOTO_LIST,
            onClick = { navController.navigate(Routes.PHOTO_LIST) { launchSingleTop = true } },
            icon = {
                val iconRes = if (currentRoute == Routes.PHOTO_LIST)
                    R.drawable.ic_nav_home_selected
                else
                    R.drawable.ic_nav_home_normal

                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = "홈",
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute(navController) == Routes.FAVORITE_LIST,
            onClick = { navController.navigate(Routes.FAVORITE_LIST) },
            icon = {
                val iconRes = if (currentRoute == Routes.FAVORITE_LIST)
                    R.drawable.ic_nav_favorite_selected
                else
                    R.drawable.ic_nav_favorite_normal

                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = "즐겨찾기",
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}