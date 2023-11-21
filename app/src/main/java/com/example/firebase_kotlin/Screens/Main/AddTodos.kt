package com.example.firebase_kotlin.Screens.Main

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.firebase_kotlin.Models.Todo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodos(navController: NavController) {
    var buttonLoading by remember {
        mutableStateOf(false)
    }

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var selectedImagePath by remember {
        mutableStateOf<Uri?>(null)
    }

    var singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { selectedImagePath = it }
    )

    val storage = Firebase.storage
    val storageRef = storage.reference





    Scaffold(bottomBar = {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.Start),
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },

                    ) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = "Favorite",
                        tint = Color.Gray // Optional: Set icon color
                    )
                }
                if (buttonLoading) {
                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .padding(bottom = 5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RectangleShape,
                        onClick = {}

                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    var context= LocalContext.current
                    fun getRealPathFromUri(context: Context, uri: Uri): String? {
                        var filePath: String?=""
                        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                            cursor.moveToFirst()
                            val columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                            filePath = cursor.getString(columnIndex)
                        }
                        return filePath
                    }
                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .padding(bottom = 5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RectangleShape,
                        onClick = {
                            buttonLoading = true
                            val time = System.currentTimeMillis()
                            if (selectedImagePath != null) {


                                val imageRef =
                                    storageRef.child("images/${FirebaseAuth.getInstance().currentUser?.uid}/${time}.jpg")
                                var filePath:String?=getRealPathFromUri(context,selectedImagePath!!)
                                val file = Uri.fromFile(File(filePath))

                                // Upload the file to Firebase Storage
                                val uploadTask = imageRef.putFile(file)

                                uploadTask.addOnSuccessListener {

                                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                                        val downloadUrl = uri.toString()
                                        val time = System.currentTimeMillis()
                                        FirebaseFirestore.getInstance().collection("Todo")
                                            .document(time.toString())
                                            .set(
                                                Todo(
                                                    title = title,
                                                    description = description,
                                                    id = time.toString(),
                                                    imageURL = downloadUrl
                                                )
                                            ).addOnSuccessListener {
                                                buttonLoading = false
                                            }

                                    }.addOnFailureListener { exception ->

                                    }
                                }.addOnFailureListener { exception ->

                                }
                            } else {

                                FirebaseFirestore.getInstance().collection("Todo")
                                    .document(time.toString())
                                    .set(
                                        Todo(
                                            title = title,
                                            description = description,
                                            id = time.toString(),

                                            )
                                    ).addOnSuccessListener {
                                        buttonLoading = false
                                    }
                            }
                        }

                    ) {
                        Text(text = "Add Task")
                    }
                }
            }
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(it), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = {
                    Text(
                        text = "Enter Title"
                    )
                })
            Spacer(modifier = Modifier.height(15.dp))
            TextField(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                singleLine = false,
                maxLines = 4,
                label = {
                    Text(text = "Enter description")
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            AsyncImage(
                model = selectedImagePath,

                contentDescription = "poster",
                modifier = Modifier

                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
    }
}