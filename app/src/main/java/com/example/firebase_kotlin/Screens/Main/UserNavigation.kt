package com.example.firebase_kotlin.Screens.Main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.firebase_kotlin.BottomNavigationItem
import com.example.firebase_kotlin.Navigation.NavigationScreens
import com.example.firebase_kotlin.Navigation.Navigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,

            ),
        BottomNavigationItem(
            title = "Add",
            selectedIcon = Icons.Filled.CheckCircle,
            unselectedIcon = Icons.Outlined.CheckCircle,

            ),
        BottomNavigationItem(
            title = "Favourites",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,

            ),

        )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(bottomBar = {
        NavigationBar(
            modifier = Modifier.height(70.dp),
            containerColor = Color(0xff2F64F9),
            contentColor = Color(0xff2F64F9),


            ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        selectedTextColor=Color.White,
                        indicatorColor = Color(0xff2F64F9),
                    ),
                    selected = selectedItemIndex == index,

                    onClick = {
                        selectedItemIndex = index
                        when (index) {
                            0 -> {
                                navController.navigate(NavigationScreens.Home.name)
                            }

                            1 -> {
                                navController.navigate(NavigationScreens.CompletedTodos.name)
                            }

                            2 -> {
                                navController.navigate(NavigationScreens.Profile.name)
                            }
                        }
                    },
                    label = {
                        Text(text = item.title)
                    },
                    alwaysShowLabel = false,
                    icon = {

                        Icon(
                            tint = Color.White,
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )

                    }
                )
            }
        }
    }) {

        Navigator(
            navController = navController,
            startDestination = NavigationScreens.Home.name,
            padding = it
        )


    }


}