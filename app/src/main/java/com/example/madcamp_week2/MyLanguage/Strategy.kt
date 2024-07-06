package com.example.madcamp_week2.MyLanguage

import com.example.madcamp_week2.Stock

class Strategy (
    var title: String,
    var related_stock: List<Stock>,
    var actionList: List<Action>,

){
    val stockHoldMap: MutableMap<String, Float> = mutableMapOf()
    fun calculate(startDate: String, endDate: String, initialCash:Int): Float{
        val tradeReqeustList = mutableListOf<TradeRequest>()
        for (action in actionList){
            tradeReqeustList.addAll(action.getAllRequests(startDate, endDate))
        }

        // sort by date and calculate 수익률
        return 100f
    }
}



