package com.example.firebase_kotlin.Navigation

enum class NavigationScreens {
    Home, Profile, CompletedTodos, Login, Signup,UserNavigation;

    companion object {
        fun fromRoute(route: String?): NavigationScreens = when (route?.substringBefore("/")) {
            Home.name -> Home
            Profile.name -> Profile
            CompletedTodos.name -> CompletedTodos
            Login.name -> Login
            Signup.name -> Signup
            UserNavigation.name->UserNavigation
            else -> {
                throw IllegalArgumentException("Route $route is not defined")
            }
        }
    }
}