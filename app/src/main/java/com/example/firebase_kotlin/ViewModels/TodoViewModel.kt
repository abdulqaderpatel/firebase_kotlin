package com.example.firebase_kotlin.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.firebase_kotlin.Models.Todo

class TodoViewModel:ViewModel()
{
    var todoList= mutableStateListOf<Todo>()
    var isListDataChanged=mutableStateOf(true)

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