package com.example.madcamp_week2.MyLanguage

import android.util.Log
import com.example.madcamp_week2.stockData

abstract class MyFloat {
    abstract fun evaluate(stockPriceMap: Map<String, stockData>): Float
}

enum class FloatOperator (val calculate: (Float, Float) -> Float) {
    ADD({ x, y -> x + y }),
    SUB({ x, y -> x - y }),
    MUL({ x, y -> x * y }),
    DIV({ x, y -> x / y });
}
class MyFloatBinaryOp(
    private val rightOperand: MyFloat,
    private val leftOperand: MyFloat,
    private val floatOperator : FloatOperator
): MyFloat(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Float
            = floatOperator.calculate(rightOperand.evaluate(stockPriceMap), leftOperand.evaluate(stockPriceMap))
}

class MyStockPrice(
    val stockID: String
): MyFloat(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Float {
        Log.d("MyInt", "stockprice is: ${stockPriceMap[stockID]}")
        return stockPriceMap[stockID]?.stck_clpr?.toFloat() ?: 0f
    }
}

class MyNum(
    private val num: Float
): MyFloat() {
    override fun evaluate(stockPriceMap: Map<String, stockData>): Float = num
}

