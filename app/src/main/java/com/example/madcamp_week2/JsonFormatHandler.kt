package com.example.madcamp_week2


import com.example.madcamp_week2.MyLanguage.Strategy
import com.google.gson.Gson
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

fun strategyListToJson(strategyList: List<Strategy>): String{
    //val mapper = jac
    return Json.encodeToString(ListSerializer(Strategy.serializer()), strategyList)
}


fun jsonToStrategyList(json: String): List<Strategy>{
    // val parsedItem = Json.decodeFromString<Strategy>(json)
    return Json.decodeFromString<List<Strategy>>(json)
}