package com.example.madcamp_week2

class Stock (
    val id : String,
    val name: String,
    val market: String
){
    override fun toString(): String = this.name
}