package com.example.firebase_kotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.firebase_kotlin.Navigation.NavigationScreens
import com.example.firebase_kotlin.Navigation.Navigator
import com.example.firebase_kotlin.Screens.Main.UserNavigation
import com.example.firebase_kotlin.ViewModels.UserViewModel
import com.google.firebase.auth.FirebaseAuth


import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,

    )

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
    @SuppressLint(
        "CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter",
        "SuspiciousIndentation"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            var userViewModel = UserViewModel()
            val navController = rememberNavController()
            userViewModel.isLoggedIn.value = FirebaseAuth.getInstance().currentUser?.uid != null
            when (userViewModel.isLoggedIn.value) {
                false -> {

                    Navigator(
                        navController = navController,
                        startDestination = NavigationScreens.Login.name
                    )
                }

                else -> {
                    UserNavigation()

                }
            }
        }
    }
}






