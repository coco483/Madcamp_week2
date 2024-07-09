package com.example.madcamp_week2.Class

import kotlinx.serialization.Serializable

@Serializable
class Stock (
    val id : String,
    val name: String,
    val market: String,
){
    override fun toString(): String = this.name
}