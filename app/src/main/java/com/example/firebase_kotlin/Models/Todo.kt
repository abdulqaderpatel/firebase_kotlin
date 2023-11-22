package com.example.firebase_kotlin.Models

data class Todo(
    var userId: String?,
    var category: String,
    var time: String,
    var title: String,
    var description: String,
    var imageURL: String = "",
    var completed: Boolean = false,
    var color: String,


    )
