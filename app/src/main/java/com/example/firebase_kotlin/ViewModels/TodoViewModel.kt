package com.example.firebase_kotlin.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.firebase_kotlin.Models.Category
import com.example.firebase_kotlin.Models.Todo

class TodoViewModel:ViewModel()
{
    var todoList= mutableStateListOf<Todo>()
    var isListDataChanged=mutableStateOf(true)
    var categoryList= mutableStateListOf<Category>()
    var showBottomSheet=mutableStateOf(false)

    fun getTodos():List<Todo>
    {
        return todoList.filter {
            !it.completed
        }
    }

    fun getCompletedTodos():List<Todo>
    {
        var completedTodos=todoList.filter {
            it.completed
        }

        return completedTodos
    }
}