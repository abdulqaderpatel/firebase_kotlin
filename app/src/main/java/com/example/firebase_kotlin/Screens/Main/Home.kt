package com.example.firebase_kotlin.Screens.Main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.firebase_kotlin.Models.Todo
import com.example.firebase_kotlin.Navigation.NavigationScreens
import com.example.firebase_kotlin.ViewModels.TodoViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController, todoViewModel: TodoViewModel) {

    var isFABVisible by remember { mutableStateOf(true) }

    if(todoViewModel.isListDataChanged.value) {
        LaunchedEffect(Unit)
        {
            todoViewModel.todoList.clear()
            val querySnapshot = FirebaseFirestore.getInstance().collection("Todo").get().await()
            for (document in querySnapshot.documents) {
                val data = document.data
                val id = data?.get("id") as String
                val title = data?.get("title") as String
                val description = data?.get("description") as String
                val imageURL = data?.get("imageURL") as String
                val completed = data.get("completed") as Boolean
                todoViewModel.todoList.add(
                    Todo(
                        title = title,
                        description = description,
                        id = id,
                        imageURL = imageURL,
                        completed = completed

                    )
                )
            }
            todoViewModel.isListDataChanged.value=false
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
                .padding(10.dp)
                .height(650.dp)
                .fillMaxWidth(),

            )
        {
            itemsIndexed(todoViewModel.getTodos())
            { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)


                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row {
                            if(item.imageURL!="") {
                                Image(
                                    painter = rememberAsyncImagePainter(item.imageURL),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape),
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                            }
                            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(modifier = Modifier.align(Alignment.Start), text = item.description)



                    }
                }

            }
        }
    }
}