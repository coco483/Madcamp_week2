package com.example.madcamp_week2.MyLanguage

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.madcamp_week2.Stock
import com.example.madcamp_week2.getHistoryData
import com.example.madcamp_week2.getOneMonthBefore
import com.example.madcamp_week2.parseHistoryData
import com.example.madcamp_week2.stockData
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Strategy (
    val title: String,
    val related_stockID: List<String>,
    val actionList: List<Action>,
){

    fun calculate(startDate: String, endDate: String, initialCash:Int): Double{
        if (related_stockID.isEmpty()) Log.d("Action", "There should be one stock involved")

        // request all stock price data from api
        val allStockData: MutableMap<String, Map<String, stockData>> = mutableMapOf()
        // allStockData : stockID -> { date -> stockData }
        for (stockID in related_stockID.toSet()){
            val dailyStockJson = getHistoryData(stockID, startDate, endDate, 'D')
            val dailyStockData = parseHistoryData(dailyStockJson!!)
            allStockData[stockID] = dailyStockData.associateBy { it.stck_bsop_date }
            Log.d("Strategy", "involved stock ${stockID} added: $dailyStockJson")
        }
        // get all tradeRequest from all actions
        val tradeRequestList = mutableListOf<TradeRequest>()
        for (action in actionList){
            val actionInvolvedStockData =  allStockData.filterKeys { it in action.involvedStockIdList }
            tradeRequestList.addAll(action.getAllRequests(startDate, endDate, actionInvolvedStockData))
        }
        Log.d("StrategyCalculate", "$tradeRequestList")
        // sort by date and calculate 수익률
        tradeRequestList.sortBy { it.date }
        val capitalHandler = CapitalHandler(initialCash)
        for (request in tradeRequestList){
            capitalHandler.processRequest(request, allStockData)
        }
        return capitalHandler.calculateCapital(endDate, allStockData) / initialCash.toDouble() * 100 - 100
    }

    private class CapitalHandler (
        private var cashAmount: Int,
        private val stockHoldMap: MutableMap<String, Float> = mutableMapOf(),
    ) {
        fun processRequest(tradeRequest: TradeRequest, allStockData: Map<String, Map<String, stockData>>){
            when(tradeRequest.tradeType){
                TradeType.BUY -> {
                    val stockPrice = allStockData[tradeRequest.stockId]!![tradeRequest.date]!!.stck_clpr
                    val buyCashAmount = minOf(cashAmount, (stockPrice*tradeRequest.stockAmount).toInt())
                    val buyStockAmount = buyCashAmount / stockPrice
                    cashAmount -= buyCashAmount
                    val currentStockAmount = stockHoldMap.getOrDefault(tradeRequest.stockId, 0.0)
                    stockHoldMap[tradeRequest.stockId] = currentStockAmount.toFloat() + buyStockAmount.toFloat()
                }
                TradeType.SELL -> {
                    if (stockHoldMap[tradeRequest.stockId] != null){
                        val stockPrice = allStockData[tradeRequest.stockId]!![tradeRequest.date]!!.stck_clpr
                        val sellStockAmount = minOf(tradeRequest.stockAmount, stockHoldMap[tradeRequest.stockId]!!)
                        val sellCashAmount = stockPrice * sellStockAmount
                        stockHoldMap[tradeRequest.stockId] = stockHoldMap[tradeRequest.stockId]!! - sellStockAmount
                        cashAmount += sellCashAmount.toInt()
                    }
                }
            }
        }
        fun calculateCapital(endDate: String, allStockData: Map<String, Map<String, stockData>>): Int{
            Log.d("StrategyCalculate", "$cashAmount, $stockHoldMap")
            var currentCapitalValue = cashAmount
            for ((stockId, holdAmount) in stockHoldMap){
                val stockPrice = allStockData[stockId]!!.maxBy { it.key }.value.stck_clpr
                currentCapitalValue += (stockPrice * holdAmount).toInt()
            }
            return currentCapitalValue
        }
    }
}



