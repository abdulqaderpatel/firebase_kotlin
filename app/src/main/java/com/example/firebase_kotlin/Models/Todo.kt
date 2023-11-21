package com.example.firebase_kotlin.Models

data class Todo(
    var id: String,
    var title: String,
    var description: String,
    var imageURL: String = "",
    var completed: Boolean = false

)
