package com.example.firebase_kotlin.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.firebase_kotlin.Screens.Authentication.Login
import com.example.firebase_kotlin.Screens.Authentication.Signup
import com.example.firebase_kotlin.Screens.Main.AddTodos
import com.example.firebase_kotlin.Screens.Main.CompletedTodos
import com.example.firebase_kotlin.Screens.Main.Home
import com.example.firebase_kotlin.Screens.Main.Profile
import com.example.firebase_kotlin.Screens.Main.TodoCategories
import com.example.firebase_kotlin.Screens.Main.UserNavigation
import com.example.firebase_kotlin.ViewModels.TodoViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigator(
    navController: NavHostController,
    startDestination: String,
    padding: PaddingValues = PaddingValues(0.dp)
) {
    var todoViewModel = TodoViewModel()
    NavHost(
        modifier = Modifier.padding(padding),
        navController = navController,
        startDestination = startDestination
    )
    {
        composable(NavigationScreens.Login.name)
        {
            Login(navController = navController)
        }
        composable(NavigationScreens.Signup.name)
        {
            Signup(navController = navController)
        }
        composable(NavigationScreens.Home.name)
        {
            Home(navController = navController, todoViewModel)
        }
        composable(NavigationScreens.CompletedTodos.name)
        {
            CompletedTodos(navController = navController, todoViewModel)
        }
        composable(NavigationScreens.Profile.name)
        {
            Profile(navController = navController)
        }
        composable(NavigationScreens.UserNavigation.name)
        {
            UserNavigation()
        }
        composable(NavigationScreens.AddTodos.name)
        {
            AddTodos(todoViewModel)
        }
        composable(NavigationScreens.TodoCategories.name)
        {
            TodoCategories(navController = navController, todoViewModel = todoViewModel)
        }
    }
}