package com.example.madcamp_week2.MyLanguage

import com.example.madcamp_week2.stockData

abstract class MyBool {
    abstract fun evaluate(stockPriceMap : Map<String, stockData>): Boolean
}

class MyAnd  (
    private val rightOperand: MyBool,
    private val leftOperand: MyBool
): MyBool(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Boolean
    = rightOperand.evaluate(stockPriceMap) && leftOperand.evaluate(stockPriceMap)
}

class MyOr  (
    private val rightOperand: MyBool,
    private val leftOperand: MyBool
): MyBool(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Boolean
    = rightOperand.evaluate(stockPriceMap) || leftOperand.evaluate(stockPriceMap)
}

class MyNot  (
    private val operand: MyBool,
): MyBool(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Boolean
    = !(operand.evaluate(stockPriceMap))
}

class MyCompare (
    private val rightOperand: MyFloat,
    private val leftOperand: MyFloat,
    private val comparator: Comparator
): MyBool(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Boolean
    = comparator.compare(rightOperand.evaluate(stockPriceMap), leftOperand.evaluate(stockPriceMap))
}

enum class Comparator(val compare: (Float, Float) -> Boolean) {
    GT({ x, y -> x > y }),
    LT({ x, y -> x < y }),
    GTE({ x, y -> x >= y }),
    LTE({ x, y -> x <= y });
}


