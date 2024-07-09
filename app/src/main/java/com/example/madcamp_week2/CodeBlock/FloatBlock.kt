package com.example.madcamp_week2.CodeBlock

import android.content.Context
import android.widget.Toast
import com.example.madcamp_week2.MyLanguage.FloatOperator
import com.example.madcamp_week2.MyLanguage.MyFloat

abstract class FloatBlock {
    abstract fun getMyFloat(context: Context): Pair<MyFloat, List<String>>?
}
class FloatBinaryOpBlock : FloatBlock(){
    var rightOpBlock: FloatBlock? = null
    var leftOpBlock: FloatBlock? = null
    var floatOperator: FloatOperator? = null
    override fun getMyFloat(context: Context): Pair<MyFloat, List<String>>? {
        if (rightOpBlock == null) {
            Toast.makeText(context, "please fill in the right operand", Toast.LENGTH_LONG).show()
        } else if (leftOpBlock == null){
            Toast.makeText(context, "please fill in the left operand", Toast.LENGTH_LONG).show()
        } else {
            val (rightOperand, list1) = rightOpBlock!!.getMyFloat(context) ?: Pair(null, listOf())
            val (leftOperand, list2) = leftOpBlock!!.getMyFloat(context) ?: Pair(null, listOf())
            if (rightOperand != null && leftOperand != null && floatOperator != null) {
                return Pair(MyFloat.MyFloatBinaryOp(rightOperand, leftOperand, floatOperator!!), (list1+list2))
            }
        }
        return null
    }
}

class NumBlock : FloatBlock(){
    var num: Float = 0f
    override fun getMyFloat(context: Context): Pair<MyFloat, List<String>>? {
        return Pair(MyFloat.MyNum(num), listOf())
    }
}

class StockPriceBlock: FloatBlock() {
    var stockID: String? = null
    override fun getMyFloat(context: Context): Pair<MyFloat, List<String>>? {
        return stockID?.let { id -> Pair(MyFloat.MyStockPrice(id), listOf(id)) }
    }
}