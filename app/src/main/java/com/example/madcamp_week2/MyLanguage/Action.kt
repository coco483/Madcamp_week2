package com.example.madcamp_week2.MyLanguage

import android.util.Log
import com.example.madcamp_week2.Stock
import com.example.madcamp_week2.stockData
import kotlinx.serialization.Serializable

@Serializable
data class Action(
    val condition: MyBool,
    val tradePlan: TradePlan,
    val involvedStockIdList: List<String>
) {
    fun getAllRequests(startDate:String, endDate:String, involvedStockData: Map<String, Map<String, stockData>>): List<TradeRequest> {
        //involvedStockData: stockID -> { date -> stockData }

        // find all trade requests that will happen
        val firstEntry = involvedStockData.entries.first()
        val numResponse = firstEntry.value.size
        Log.d("Action", "allStockData: $involvedStockData")
        Log.d("Action", "num_response is $numResponse")

        // convert into date -> { stockID -> stockData }
        val dateToStockDataMap: MutableMap<String, MutableMap<String, stockData>> = mutableMapOf()
        for ((stockID, dateMap) in involvedStockData) {
            for ((date, stockData) in dateMap) {
                val stockPriceMap = dateToStockDataMap.getOrPut(date) { mutableMapOf() }
                stockPriceMap[stockID] = stockData
            }
        }
        // collect all possible tradeRequest
        val tradeRequestList: MutableList<TradeRequest> = mutableListOf()
        for ((date,stockPriceMap) in dateToStockDataMap){
            if (condition.evaluate(stockPriceMap)) {
                val request = tradePlan.makeTradeRequest(date, stockPriceMap)
                tradeRequestList.add(request)
            }
        }
        return tradeRequestList
    }
}

enum class TradeType {BUY, SELL}

class TradeRequest(
    val date: String,
    val tradeType: TradeType,
    val stockId: String,
    val stockAmount: Float
)
@Serializable
data class TradePlan (
    val tradeType: TradeType,
    val stockId: String,
    val amount: MyFloat
){
    fun makeTradeRequest(date:String, stockPriceMap: Map<String, stockData>):TradeRequest{
        return TradeRequest(date, tradeType, stockId, amount.evaluate(stockPriceMap))
    }
}