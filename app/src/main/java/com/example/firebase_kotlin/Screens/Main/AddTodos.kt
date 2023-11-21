package com.example.firebase_kotlin.Screens.Main

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.firebase_kotlin.Models.Todo
import com.example.firebase_kotlin.ViewModels.TodoViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodos(navController: NavController, todoViewModel: TodoViewModel) {
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

    var colors = listOf<Color>(
        Color.Green,
        Color.Red,
        Color.Blue,
        Color.Yellow,
        Color.Magenta,
        Color.LightGray,
        Color.DarkGray,
        Color.Cyan
    )

    var selectedColorIndex by remember {
        mutableStateOf(Color.Green)
    }

    var textColors = listOf<Color>(
        Color.Black,
        Color.White,
        Color.White,
        Color.Black,
        Color.White,
        Color.Black,
        Color.White,
        Color.Black


    )

    var selectedTextColorIndex by remember {
        mutableStateOf(Color.Black)
    }

    var colorIndex by remember {
        mutableStateOf(0)
    }

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
                        colors = ButtonDefaults.buttonColors(containerColor = selectedColorIndex),
                        shape = RectangleShape,
                        onClick = {}

                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(20.dp)
                                .width(20.dp)
                        )
                    }
                } else {
                    var context = LocalContext.current
                    fun getRealPathFromUri(context: Context, uri: Uri): String? {
                        var filePath: String? = ""
                        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                            cursor.moveToFirst()
                            val columnIndex =
                                cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                            filePath = cursor.getString(columnIndex)
                        }
                        return filePath
                    }
                    Button(
                        modifier = Modifier
                            .width(250.dp)
                            .padding(bottom = 5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = selectedColorIndex),
                        shape = RectangleShape,
                        onClick = {
                            buttonLoading = true
                            val time = System.currentTimeMillis()
                            if (selectedImagePath != null) {


                                val imageRef =
                                    storageRef.child("images/${FirebaseAuth.getInstance().currentUser?.uid}/${time}.jpg")
                                var filePath: String? =
                                    getRealPathFromUri(context, selectedImagePath!!)
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
                                                    imageURL = downloadUrl,
                                                    color = colorIndex.toString()

                                                    )
                                            ).addOnSuccessListener {
                                                buttonLoading = false
                                                todoViewModel.isListDataChanged.value = true
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
                                            color = colorIndex.toString()


                                            )
                                    ).addOnSuccessListener {
                                        buttonLoading = false
                                        todoViewModel.isListDataChanged.value = true
                                    }
                            }
                        }

                    ) {
                        Text(
                            text = "Add Task",
                            style = MaterialTheme.typography.titleMedium,
                            color = selectedTextColorIndex
                        )
                    }
                }
            }
        }
    }) { pad ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(pad), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = {
                    Text(
                        text = "Enter Title",
                        style = MaterialTheme.typography.titleMedium,
                        color = selectedTextColorIndex
                    )

                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = selectedColorIndex,
                    textColor = selectedTextColorIndex
                )
            )
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
                    Text(
                        text = "Enter Description",
                        style = MaterialTheme.typography.titleSmall,
                        color = selectedTextColorIndex
                    )

                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = selectedColorIndex,
                    textColor = selectedTextColorIndex
                ),
            )

            Spacer(modifier = Modifier.height(15.dp))
            AsyncImage(
                model = selectedImagePath,

                contentDescription = "poster",
                modifier = Modifier

                    .fillMaxWidth()
                    .height(320.dp)
                    .align(Alignment.End),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(15.dp))
            LazyRow(modifier = Modifier.height(50.dp))
            {


                itemsIndexed(colors)
                { index, color ->
                    var modif = Modifier
                        .padding(10.dp)
                        .clickable {
                            selectedColorIndex = color
                            selectedTextColorIndex = textColors[index]
                            colorIndex = index+1
                        }
                        .clip(shape = CircleShape)
                        .size(25.dp)
                    if (selectedColorIndex.equals(color)) {
                        modif = Modifier
                            .padding(4.dp)
                            .border(border = BorderStroke(color = Color.Black, width = 2.dp))
                            .clickable {
                                selectedColorIndex = color
                                selectedTextColorIndex = textColors[index]
                                colorIndex = index+1
                            }
                            .clip(shape = CircleShape)
                            .size(25.dp)
                    }
                    Card(
                        modifier = modif, colors = CardDefaults.cardColors(containerColor = color)
                    ) {

                    }
                }
            }
        }


    }
}
