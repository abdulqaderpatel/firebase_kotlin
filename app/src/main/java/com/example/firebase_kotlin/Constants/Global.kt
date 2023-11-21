package com.example.firebase_kotlin.Constants

import androidx.compose.ui.graphics.Color

class Global {
    fun getContainerColorFromDatabase(color: String): Color {

        var containerColor = when (color) {
            "1" -> Color.Green
            "2" -> Color.Red
            "3" -> Color.Blue
            "4" -> Color.Yellow
            "5" -> Color.Magenta
            "6" -> Color.LightGray
            "7" -> Color.DarkGray
            else -> {
                Color.Cyan
            }
        }
        return containerColor
    }

    fun getTextColorFromDatabase(color: String): Color {


        var textColor = when (color) {
            "1" -> Color.Black
            "2" -> Color.White
            "3" -> Color.White
            "4" -> Color.Black
            "5" -> Color.White
            "6" -> Color.Black
            "7" -> Color.White
            else -> {
                Color.Black
            }
        }
        return textColor
    }

}



