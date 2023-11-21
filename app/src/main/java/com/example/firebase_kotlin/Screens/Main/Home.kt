package com.example.firebase_kotlin.Screens.Main

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    var isFABVisible by remember { mutableStateOf(true) }

    Scaffold(floatingActionButton = {
        if(isFABVisible) {
            FloatingActionButton(
                onClick = { },
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    }) {

    }
}