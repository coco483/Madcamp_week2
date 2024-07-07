package com.example.madcamp_week2.MyLanguage

import android.util.Log
import com.example.madcamp_week2.stockData

abstract class MyFloat {
    abstract fun evaluate(stockPriceMap: Map<String, stockData>): Float
}

class MyAdd(
    private val rightOperand: MyFloat,
    private val leftOperand: MyFloat
): MyFloat(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Float
    = rightOperand.evaluate(stockPriceMap) + leftOperand.evaluate(stockPriceMap)
}

class MySub(
    private val rightOperand: MyFloat,
    private val leftOperand: MyFloat
): MyFloat(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Float
    = rightOperand.evaluate(stockPriceMap) - leftOperand.evaluate(stockPriceMap)
}

class MyMultiply(
    private val rightOperand: MyFloat,
    private val leftOperand: MyFloat
): MyFloat(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Float
    = rightOperand.evaluate(stockPriceMap) * leftOperand.evaluate(stockPriceMap)
}

class StockPrice(
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

