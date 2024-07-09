package com.example.madcamp_week2.CodeBlock

import android.content.Context
import android.widget.Toast
import com.example.madcamp_week2.Class.Stock
import com.example.madcamp_week2.MyLanguage.FloatOperator
import com.example.madcamp_week2.MyLanguage.MyBool
import com.example.madcamp_week2.MyLanguage.MyFloat

abstract class FloatBlock {
    abstract fun getMyFloat(context: Context): Pair<MyFloat, List<Stock>>?
}
class FloatBinaryOpBlock : FloatBlock(){
    var rightOpBlock: FloatBlock? = null
    var leftOpBlock: FloatBlock? = null
    var floatOperator: FloatOperator? = null
    override fun getMyFloat(context: Context): Pair<MyFloat, List<Stock>>? {
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
    fun setAllChild(myFloatBinaryOp: MyFloat.MyFloatBinaryOp){
        rightOpBlock = setAllChildOfFloat(myFloatBinaryOp.rightOperand)
        leftOpBlock = setAllChildOfFloat(myFloatBinaryOp.leftOperand)
        floatOperator = myFloatBinaryOp.floatOperator
    }
}

class NumBlock : FloatBlock(){
    var num: Float? = null
    override fun getMyFloat(context: Context): Pair<MyFloat, List<Stock>>? {
        if (num == null) {
            Toast.makeText(context, "please fill in the number", Toast.LENGTH_SHORT).show()
            return null
        }
        else return Pair(MyFloat.MyNum(num!!), listOf())
    }
    fun setAllChild(myNum: MyFloat.MyNum){
        num = myNum.num
    }
}

class StockPriceBlock: FloatBlock() {
    var stock: Stock? = null
    override fun getMyFloat(context: Context): Pair<MyFloat, List<Stock>>? {
        if (stock == null) {
            Toast.makeText(context, "please choose the stock", Toast.LENGTH_SHORT).show()
            return null
        } else return Pair(MyFloat.MyStockPrice(stock!!), listOf(stock!!))
    }
    fun setAllChild(myStockPrice: MyFloat.MyStockPrice){
        stock = myStockPrice.stock
    }
}