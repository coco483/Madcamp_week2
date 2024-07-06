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
        val tradeRequestList = mutableListOf<TradeRequest>()

        // request all stock price data from api
        val allStockData: MutableList<Pair<String, List<stockData>>> = mutableListOf()
        for (stock in involvedStocks){
            val dailyStockJson = getHistoryData(stock.id, startDate, endDate, 'D')
            val dailyStockData = parseHistoryData(dailyStockJson!!)
            allStockData.add(Pair(stock.id, dailyStockData))
            Log.d("Action", "involved stock ${stock.name} added: $dailyStockJson")
        }
        // find all trade requests that will happen
        val num_response = allStockData[0].second.size
        Log.d("Action", "allStockData: $allStockData")
        Log.d("Action", "num_response is $num_response")
        for (i in 0 until num_response){
            val stockPriceMap : MutableMap<String, Float> = mutableMapOf()
            for ((stockID, dailyStockData) in allStockData){
                stockPriceMap[stockID] = dailyStockData[i].stck_clpr.toFloat()
            }
            if (condition.evaluate(stockPriceMap)) {
                val request = tradePlan.makeTradeRequest(allStockData[0].second[i].stck_bsop_date, stockPriceMap)
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
    val stock: Stock,
    val stockAmount: Float
)

class TradePlan (
    val tradeType: TradeType,
    val stock: Stock,
    val amount: MyFloat
){
    fun makeTradeRequest(date:String, stockPriceMap: Map<String, Float>):TradeRequest{
        return TradeRequest(date, tradeType, stock, amount.evaluate(stockPriceMap))
    }
}