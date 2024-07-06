package com.example.madcamp_week2.MyLanguage

import com.example.madcamp_week2.Stock

abstract class MyInt {
    abstract val isStatic: Boolean
    abstract val value: Int
    abstract val _value:Int?
    abstract fun evaluate(): Int
}

class MyAdd(
    private val rightOperand: MyInt,
    private val leftOperand: MyInt
): MyInt(){
    override val isStatic = rightOperand.isStatic && leftOperand.isStatic
    override val _value = if (isStatic) evaluate() else null
    override val value: Int
        get() = _value ?: evaluate()
    override fun evaluate(): Int = rightOperand.evaluate() + leftOperand.evaluate()
}

class MySub(
    private val rightOperand: MyInt,
    private val leftOperand: MyInt
): MyInt(){
    override val isStatic = rightOperand.isStatic && leftOperand.isStatic
    override val _value = if (isStatic) evaluate() else null
    override val value: Int
        get() = _value ?: evaluate()
    override fun evaluate(): Int = rightOperand.evaluate() - leftOperand.evaluate()
}

class MyMultiply(
    private val rightOperand: MyInt,
    private val leftOperand: MyInt
): MyInt(){
    override val isStatic = rightOperand.isStatic && leftOperand.isStatic
    override val _value = if (isStatic) evaluate() else null
    override val value: Int
        get() = _value ?: evaluate()
    override fun evaluate(): Int = rightOperand.evaluate() * leftOperand.evaluate()
}

class StockPrice(
    val stock: Stock
): MyInt(){
    override val isStatic = false
    override val _value = null
    override val value: Int
        get() = evaluate()
    override fun evaluate(): Int = 1
}
