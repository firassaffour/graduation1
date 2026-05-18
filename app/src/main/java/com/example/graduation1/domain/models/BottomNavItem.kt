package com.example.graduation1.domain.models

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.graduation1.R

import com.example.graduation1.ui.theme.primaryRed

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val title: String
) {
    object Home : BottomNavItem("homeScreen", R.drawable.home, "Home")
    object Friends : BottomNavItem("friendsScreen", R.drawable.friends, "Friends")
    object Create : BottomNavItem("createScreen", R.drawable.create, "Create")
    object Chat : BottomNavItem("chatScreen", R.drawable.chat, "Chat")
    object Profile : BottomNavItem("profileScreen", R.drawable.profile, "Profile")

    companion object {
        val items = listOf(Home, Friends, Create, Chat, Profile)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(tonalElevation = 10.dp,
        containerColor = MaterialTheme.colorScheme.background) {
        BottomNavItem.items.forEach { item ->

            if (item == BottomNavItem.Create){
                Box(modifier = Modifier.weight(1f))
            }

            else {
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = item.title,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    colors = NavigationBarItemColors(
                        selectedIndicatorColor = primaryRed,
                        selectedIconColor = MaterialTheme.colorScheme.onBackground,
                        selectedTextColor = MaterialTheme.colorScheme.onBackground,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        disabledIconColor = MaterialTheme.colorScheme.onBackground,
                        disabledTextColor = MaterialTheme.colorScheme.onBackground
                    ),
                    //label = { Text(item.title, color = MaterialTheme.colorScheme.onBackground, fontSize = 9.sp) },
                    selected = currentRoute == item.route,
                    onClick = {
                            navController.navigate(item.route)
                            {
                                popUpTo(BottomNavItem.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                    } /* onClick */)
            } // else
        }
    } // NavigationBar
} // BottomNavigationBar