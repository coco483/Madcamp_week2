package com.example.madcamp_week2


import com.example.madcamp_week2.MyLanguage.Strategy
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

fun StrategyToJson(strategy: Strategy): String{
    //val mapper = jac
    return Json.encodeToString(Strategy.serializer(), strategy)
}


fun JsonToStrategy(json: String): Strategy{
    // val parsedItem = Json.decodeFromString<Strategy>(json)
    return Json.decodeFromString<Strategy>(json)
}