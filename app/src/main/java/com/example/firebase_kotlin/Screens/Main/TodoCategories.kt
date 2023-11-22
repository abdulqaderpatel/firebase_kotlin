package com.example.firebase_kotlin.Screens.Main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.firebase_kotlin.Models.Category
import com.example.firebase_kotlin.Navigation.NavigationScreens
import com.example.firebase_kotlin.ViewModels.TodoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoCategories(navController: NavController, todoViewModel: TodoViewModel) {
    var isFABVisible by remember { mutableStateOf(true) }

    val colors = listOf<Color>(Color(0xFFFF0000), Color(0xFF0000FF), Color.DarkGray, Color.Black)

    LaunchedEffect(Unit) {

        todoViewModel.categoryList.clear()
        val collectionSnapshot = FirebaseFirestore.getInstance().collection("Category")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid).get().await()

        for (document in collectionSnapshot.documents) {
            val data = document.data
            val userId = data?.get("userId") as String
            val category = data?.get("category") as String

            todoViewModel.categoryList.add(
                Category(userId = userId, category = category)
            )

        }
    }
    Scaffold(floatingActionButton = {
        if (isFABVisible) {
            FloatingActionButton(
                containerColor = Color(0xff2F64F9), contentColor = Color.White,
                onClick = { navController.navigate(NavigationScreens.AddTodos.name) },
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    }) {
        LazyColumn(
            modifier = Modifier
                .padding(all = 20.dp)
                .height(650.dp)
                .fillMaxWidth(),


            ) {
            itemsIndexed(todoViewModel.categoryList) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(bottom = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = colors[index % 4]),
                    shape = RoundedCornerShape(10.dp)

                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = item.category,
                            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}