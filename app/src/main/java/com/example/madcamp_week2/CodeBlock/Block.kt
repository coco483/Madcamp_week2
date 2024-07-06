package com.example.madcamp_week2.CodeBlock

import android.content.Context
import android.widget.Toast
import com.example.madcamp_week2.MyLanguage.Action
import com.example.madcamp_week2.MyLanguage.MyBool
import com.example.madcamp_week2.MyLanguage.TradePlan
import com.example.madcamp_week2.Stock

abstract class BoolBlock {
    abstract fun getMyBool() : MyBool?
}
class TradePlanBlock {
    fun getTradePlan(): TradePlan? {
        TODO("not yet")
    }
}
class ActionBlock {
    var condition: BoolBlock? = null
    var tradePlan: TradePlanBlock? = null
    var involvedStocks: MutableList<Stock> = mutableListOf<Stock>()
    fun getAction(context: Context): Action? {
        if (condition == null) {
            Toast.makeText(context, "please fill in the condition", Toast.LENGTH_LONG).show()
            return null
        } else if (tradePlan == null){
            Toast.makeText(context, "please fill in the tradePlan", Toast.LENGTH_LONG).show()
            return null
        } else{
            val init_condition = condition!!.getMyBool()
            val init_tradePlan = tradePlan!!.getTradePlan()
            if (init_tradePlan != null && init_condition != null) {
                return Action(init_condition, init_tradePlan, involvedStocks)
            }
        }
        return null
    }
}