package com.example.madcamp_week2

import com.example.madcamp_week2.MyLanguage.Strategy
import com.google.gson.Gson

fun StrategyToJson(strategy: Strategy): String{
    val mapper = jackson
    return Gson().toJson(strategy, Strategy::class.java)
}