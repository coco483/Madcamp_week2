package com.example.madcamp_week2.Class

data class User(
    val id: String,
    val email: String?,
    val displayName: String?,
    var favorites: String?, // Assuming only IDs are sent
    var strategyList: String?
)
