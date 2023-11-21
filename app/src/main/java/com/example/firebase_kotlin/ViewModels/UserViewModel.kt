package com.example.firebase_kotlin.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class UserViewModel:ViewModel() {

    var isLoggedIn=
        mutableStateOf(FirebaseAuth.getInstance().currentUser?.email != null)


}