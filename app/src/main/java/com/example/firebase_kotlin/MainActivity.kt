package com.example.firebase_kotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.firebase_kotlin.ui.theme.Firebase_kotlinTheme
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.google.type.DateTime
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var db = FirebaseFirestore.getInstance().collection("tutorial")
            var title by remember {
                mutableStateOf("")
            }
            var description by remember {
                mutableStateOf("")
            }

            var trigger by remember {
                mutableStateOf(false)
            }

            var userDataList = remember { mutableStateListOf<todo>() }
            var isLoading by remember {
                mutableStateOf(true)
            }

            LaunchedEffect(trigger) {
                try {

                    val querySnapshot = db.get().await()
                    userDataList.clear()

                    for (document in querySnapshot.documents) {
                        val data = document.data
                        val name = data?.get("title") as String
                        val age = data?.get("description") as String
                        userDataList.add(todo(title = name, description = age))
                    }
                    trigger = false
                    isLoading = false
                } catch (e: Exception) {
                    // Handle exceptions
                }
            }


            Column {

                Button(onClick = {
                    GlobalScope.launch {
                        db.document("XFQu8szkNbhqWk92vqD9").update(
                            hashMapOf(
                                "title" to "bluetooth goal",
                                "description" to "superman it is",

                                ) as Map<String, Object>
                        )
                        trigger = !trigger
                    }
                }) {
                    Text(text = "update data")
                }

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth())
                    {
                        items(userDataList)
                        {
                            Column {
                                Text(text = it.title)
                                Text(text = it.description)
                            }

                        }


                    }
                }
            }
        }
    }
}






