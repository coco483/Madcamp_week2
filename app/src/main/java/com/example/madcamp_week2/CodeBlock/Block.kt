package com.example.madcamp_week2.CodeBlock

import android.content.Context
import android.widget.Toast
import com.example.madcamp_week2.Class.Stock
import com.example.madcamp_week2.MyLanguage.Action
import com.example.madcamp_week2.MyLanguage.TradePlan
import com.example.madcamp_week2.MyLanguage.TradeType

class TradePlanBlock {
    var tradeType: TradeType? = null
    var stock: Stock? = null
    var amountBlock: FloatBlock? = null
    fun getTradePlan(context: Context): Pair<TradePlan, List<Stock>>? {
        if (tradeType == null) {
            Toast.makeText(context, "거래 종류를 선택하세요", Toast.LENGTH_LONG).show()
        } else if (stock == null){
            Toast.makeText(context, "거래 종목을 선택하세요", Toast.LENGTH_LONG).show()
        } else if (amountBlock == null){
            Toast.makeText(context, "거래 수량을 선택하세요", Toast.LENGTH_LONG).show()
        } else {
            val (amount, idList) = amountBlock!!.getMyFloat(context) ?: Pair(null, listOf())
            if (amount != null){
                return Pair(TradePlan(tradeType!!, stock!!, amount), (idList+listOf(stock!!)))
            }
        }
        return null
    }
    fun setAllChild(tradePlan: TradePlan){
        tradeType = tradePlan.tradeType
        stock = tradePlan.stock
        amountBlock = setAllChildOfFloat(tradePlan.amount)
    }
}
class ActionBlock {
    var conditionBlock: BoolBlock? = null
    var tradePlanBlock: TradePlanBlock? = null
    fun getAction(context: Context): Action? {
        if (conditionBlock == null) {
            Toast.makeText(context, "거래 조건을 선택하세요", Toast.LENGTH_LONG).show()
        } else if (tradePlanBlock == null){
            Toast.makeText(context, "거래를 선택하세요", Toast.LENGTH_LONG).show()
        } else {
            val (condition, list1) = conditionBlock!!.getMyBool(context) ?: Pair(null, listOf())
            val (tradePlan, list2) = tradePlanBlock!!.getTradePlan(context) ?: Pair(null, listOf())
            if (tradePlan != null && condition != null) {
                return Action(condition, tradePlan, (list1 + list2))
            }
        }
        return null
    }
    fun setAllChild(action:Action){
        conditionBlock = setAllChildOfBool(action.condition)
        tradePlanBlock = TradePlanBlock()
        tradePlanBlock!!.setAllChild(action.tradePlan)
    }
}