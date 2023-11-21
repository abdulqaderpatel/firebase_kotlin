package com.example.firebase_kotlin.Screens.Authentication

import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.firebase_kotlin.Navigation.NavigationScreens
import com.example.firebase_kotlin.ViewModels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(navController: NavController) {
    var userViewModel = UserViewModel()
    ElevatedButton(onClick = {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword("sjkhjhjuoejkrman@gmail.com", "123456")
        navController.navigate(NavigationScreens.UserNavigation.name)

    }) {
        Text("create account")
    }
}