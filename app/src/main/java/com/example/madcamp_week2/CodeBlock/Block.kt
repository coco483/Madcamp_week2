package com.example.madcamp_week2.CodeBlock

import android.content.Context
import android.widget.Toast
import com.example.madcamp_week2.MyLanguage.Action
import com.example.madcamp_week2.MyLanguage.TradePlan
import com.example.madcamp_week2.MyLanguage.TradeType

class TradePlanBlock {
    var tradeType: TradeType? = null
    var stockId: String? = null
    var amountBlock: FloatBlock? = null
    fun getTradePlan(context: Context): Pair<TradePlan, List<String>>? {
        if (tradeType == null) {
            Toast.makeText(context, "please fill in the tradeType", Toast.LENGTH_LONG).show()
        } else if (stockId == null){
            Toast.makeText(context, "please fill in the trade stock Name", Toast.LENGTH_LONG).show()
        } else if (amountBlock == null){
            Toast.makeText(context, "please fill in the trade amount", Toast.LENGTH_LONG).show()
        } else {
            val (amount, idList) = amountBlock!!.getMyFloat(context) ?: Pair(null, listOf())
            if (amount != null){
                return Pair(TradePlan(tradeType!!, stockId!!, amount), (idList+listOf(stockId!!)))
            }
        }
        return null
    }
    fun setAllChild(tradePlan: TradePlan){
        tradeType = tradePlan.tradeType
        stockId = tradePlan.stockId
        amountBlock = setAllChildOfFloat(tradePlan.amount)
    }
}
class ActionBlock {
    var conditionBlock: BoolBlock? = null
    var tradePlanBlock: TradePlanBlock? = null
    fun getAction(context: Context): Action? {
        if (conditionBlock == null) {
            Toast.makeText(context, "please fill in the condition", Toast.LENGTH_LONG).show()
        } else if (tradePlanBlock == null){
            Toast.makeText(context, "please fill in the tradePlan", Toast.LENGTH_LONG).show()
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