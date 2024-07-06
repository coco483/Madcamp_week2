package com.example.madcamp_week2.MyLanguage

import android.util.Log
import com.example.madcamp_week2.Stock
import com.example.madcamp_week2.getHistoryData
import com.example.madcamp_week2.parseHistoryData
import com.example.madcamp_week2.stockData

class Action(
    val condition: MyBool,
    val tradePlan: TradePlan,
    val involvedStocks: List<Stock>
) {
    fun getAllRequests(startDate:String, endDate:String): List<TradeRequest> {
        if (involvedStocks.isEmpty()) Log.d("Action", "There should be one stock involved")
        //var allStockData = List<List<stockData>>()
        for (stock in involvedStocks){
            val dailyStockJson = getHistoryData(stock.id, startDate, endDate, 'D')
            val dailyStockData = dailyStockJson?.let { parseHistoryData(it) }
        }
        TODO("not implemented yet")
    }
}

enum class TradeType {BUY, SELL}

class TradeRequest(
    val date: String,
    val tradeType: TradeType,
    val stock: Stock,
    val stockAmount: Int
)

class TradePlan (
    val tradeType: TradeType,
    val stock: Stock,
    val amount: Int
)