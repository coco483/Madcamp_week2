package com.example.madcamp_week2.MyLanguage

import com.example.madcamp_week2.stockData

abstract class MyBool {
    abstract fun evaluate(stockPriceMap : Map<String, stockData>): Boolean
}

enum class BoolOperator (val calculate: (Boolean, Boolean) -> Boolean) {
    AND({ x, y -> x && y }),
    OR({ x, y -> x || y }),
}
class MyBoolBinaryOp(
    private val rightOperand: MyBool,
    private val leftOperand: MyBool,
    private val boolOperator : BoolOperator
): MyBool(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Boolean
            = boolOperator.calculate(rightOperand.evaluate(stockPriceMap), leftOperand.evaluate(stockPriceMap))
}

class MyNot  (
    private val operand: MyBool,
): MyBool(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Boolean
    = !(operand.evaluate(stockPriceMap))
}

enum class CompareOperator(val compare: (Float, Float) -> Boolean) {
    GT({ x, y -> x > y }),
    LT({ x, y -> x < y }),
    GTE({ x, y -> x >= y }),
    LTE({ x, y -> x <= y });
}

class MyCompare (
    private val rightOperand: MyFloat,
    private val leftOperand: MyFloat,
    private val comparator: CompareOperator
): MyBool(){
    override fun evaluate(stockPriceMap: Map<String, stockData>): Boolean
    = comparator.compare(rightOperand.evaluate(stockPriceMap), leftOperand.evaluate(stockPriceMap))
}




