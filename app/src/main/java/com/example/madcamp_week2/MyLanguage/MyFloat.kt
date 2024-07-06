package com.example.madcamp_week2.MyLanguage

import android.util.Log

abstract class MyFloat {
    abstract fun evaluate(stockPriceMap: Map<String, Float>): Float
}

class MyAdd(
    private val rightOperand: MyFloat,
    private val leftOperand: MyFloat
): MyFloat(){
    override fun evaluate(stockPriceMap: Map<String, Float>): Float
    = rightOperand.evaluate(stockPriceMap) + leftOperand.evaluate(stockPriceMap)
}

class MySub(
    private val rightOperand: MyFloat,
    private val leftOperand: MyFloat
): MyFloat(){
    override fun evaluate(stockPriceMap: Map<String, Float>): Float
    = rightOperand.evaluate(stockPriceMap) - leftOperand.evaluate(stockPriceMap)
}

class MyMultiply(
    private val rightOperand: MyFloat,
    private val leftOperand: MyFloat
): MyFloat(){
    override fun evaluate(stockPriceMap: Map<String, Float>): Float
    = rightOperand.evaluate(stockPriceMap) * leftOperand.evaluate(stockPriceMap)
}

class StockPrice(
    val stockID: String
): MyFloat(){
    override fun evaluate(stockPriceMap: Map<String, Float>): Float {
        Log.d("MyInt", "stockprice is: ${stockPriceMap[stockID]}")
        return stockPriceMap[stockID] ?: 0f
    }
}

class MyNum(
    private val num: Float
): MyFloat() {
    override fun evaluate(stockPriceMap: Map<String, Float>): Float = num
}

