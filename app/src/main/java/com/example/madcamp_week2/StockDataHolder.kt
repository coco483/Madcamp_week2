package com.example.madcamp_week2

import android.content.Context
import com.example.madcamp_week2.Class.Stock
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object StockDataHolder {
    var stockList: List<Stock>? = null
    fun initialize(context: Context) {
        if (stockList == null) {
            var jsonStr = context.getResources().openRawResource(R.raw.stock_code)
                .bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Stock>>() {}.type
            stockList = Gson().fromJson(jsonStr, listType)
        }
    }
}