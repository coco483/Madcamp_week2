package com.example.madcamp_week2.MyLanguage

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.madcamp_week2.Class.Stock
import com.example.madcamp_week2.Class.StrategyReturn
import com.example.madcamp_week2.getHistoryData
import com.example.madcamp_week2.parseHistoryData
import com.example.madcamp_week2.stockData
import kotlinx.serialization.Serializable

@Serializable
data class Strategy (
    val title: String,
    val related_stock: List<Stock>,
    val actionList: List<Action>,
    var greatestReturnRate: Double? = null
){

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculate(startDate: String, endDate: String, initialCash:Int): StrategyReturn {
        if (related_stock.isEmpty()) Log.d("Action", "There should be one stock involved")

        // request all stock price data from api
        val allStockData: MutableMap<String, Map<String, stockData>> = mutableMapOf()
        // allStockData : stockName -> { date -> stockData }
        for (stock in related_stock.toSet()){
            val dailyStockData = getHistoryData(stock.id, startDate, endDate, 'D')
            allStockData[stock.name] = dailyStockData!!.associateBy { it.stck_bsop_date }
            Log.d("Strategy", "involved stock ${stock} added: $dailyStockData")
        }
        // get all tradeRequest from all actions
        val tradeRequestList = mutableListOf<TradeRequest>()
        for (action in actionList){
            val involvedStockNameList: List<String> = action.involvedStockList.map { it.name }
            val actionInvolvedStockData =  allStockData.filterKeys { it in involvedStockNameList }
            tradeRequestList.addAll(action.getAllRequests(startDate, endDate, actionInvolvedStockData))
        }
        Log.d("StrategyCalculate", "$tradeRequestList")
        // sort by date and calculate 수익률
        tradeRequestList.sortBy { it.date }
        val capitalHandler = CapitalHandler(initialCash)
        for (request in tradeRequestList){
            capitalHandler.processRequest(request, allStockData)
        }
        return capitalHandler.calculateCapital(initialCash, endDate, allStockData)
    }

    private class CapitalHandler (
        private var cashAmount: Int,
        private val stockHoldMap: MutableMap<String, Float> = mutableMapOf(),
    ) {
        fun processRequest(tradeRequest: TradeRequest, allStockData: Map<String, Map<String, stockData>>){
            when(tradeRequest.tradeType){
                TradeType.BUY -> {
                    val stockPrice = allStockData[tradeRequest.stock.name]!![tradeRequest.date]!!.stck_clpr
                    val buyCashAmount = minOf(cashAmount, (stockPrice*tradeRequest.stockAmount).toInt())
                    val buyStockAmount = buyCashAmount / stockPrice
                    cashAmount -= buyCashAmount
                    val currentStockAmount = stockHoldMap.getOrDefault(tradeRequest.stock.name, 0.0)
                    stockHoldMap[tradeRequest.stock.name] = currentStockAmount.toFloat() + buyStockAmount.toFloat()
                }
                TradeType.SELL -> {
                    if (stockHoldMap[tradeRequest.stock.name] != null){
                        val stockPrice = allStockData[tradeRequest.stock.name]!![tradeRequest.date]!!.stck_clpr
                        val sellStockAmount = minOf(tradeRequest.stockAmount, stockHoldMap[tradeRequest.stock.name]!!)
                        val sellCashAmount = stockPrice * sellStockAmount
                        stockHoldMap[tradeRequest.stock.name] = stockHoldMap[tradeRequest.stock.name]!! - sellStockAmount
                        cashAmount += sellCashAmount.toInt()
                    }
                }
            }
        }
        fun calculateCapital(initialCash: Int, endDate: String, allStockData: Map<String, Map<String, stockData>>): StrategyReturn{
            Log.d("StrategyCalculate", "$cashAmount, $stockHoldMap")
            val _cashAmount = cashAmount
            val _stockHoldMap = deepCopyMap(stockHoldMap)
            var currentCapitalValue = cashAmount
            for ((stockName, holdAmount) in stockHoldMap){
                val stockPrice = allStockData[stockName]!!.maxBy { it.key }.value.stck_clpr
                currentCapitalValue += (stockPrice * holdAmount).toInt()
            }
            val returnRate = currentCapitalValue / initialCash.toDouble() * 100 - 10
            return StrategyReturn(returnRate, _cashAmount, _stockHoldMap)
        }

        private fun deepCopyMap(original: MutableMap<String, Float>): MutableMap<String, Float> {
            val copy = mutableMapOf<String, Float>()
            for ((key, value) in original) {
                copy[key] = value
            }
            return copy
        }
    }

}



