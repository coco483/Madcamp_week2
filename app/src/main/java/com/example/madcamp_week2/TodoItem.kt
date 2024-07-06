package com.example.madcamp_week2

data class TodoItem (
    val id: Int,
    val title: String,
    val completed: Boolean
)

// client가 server로 보내는 것