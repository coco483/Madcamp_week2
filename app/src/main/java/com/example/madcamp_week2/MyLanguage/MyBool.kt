package com.example.madcamp_week2.MyLanguage

abstract class MyBool {
    abstract val isStatic: Boolean
    abstract val value: Boolean
    abstract val _value:Boolean?
    abstract fun evaluate(): Boolean
}

class MyAnd  (
    private val rightOperand: MyBool,
    private val leftOperand: MyBool
): MyBool(){
    override val isStatic = rightOperand.isStatic && leftOperand.isStatic
    override val _value = if (isStatic) evaluate() else null
    override val value: Boolean
        get() = _value ?: evaluate()
    override fun evaluate(): Boolean = rightOperand.evaluate() && leftOperand.evaluate()
}

class MyOr  (
    private val rightOperand: MyBool,
    private val leftOperand: MyBool
): MyBool(){
    override val isStatic = rightOperand.isStatic && leftOperand.isStatic
    override val _value = if (isStatic) evaluate() else null
    override val value: Boolean
        get() = _value ?: evaluate()
    override fun evaluate(): Boolean = rightOperand.evaluate() || leftOperand.evaluate()
}

class MyNot  (
    private val operand: MyBool,
): MyBool(){
    override val isStatic = operand.isStatic
    override val _value = if (isStatic) evaluate() else null
    override val value: Boolean
        get() = _value ?: evaluate()
    override fun evaluate(): Boolean = !(operand.evaluate())
}

class MyCompare (
    private val rightOperand: MyInt,
    private val leftOperand: MyInt,
    private val compare_operator: MyCompareOperator
): MyBool(){
    override val isStatic = rightOperand.isStatic && leftOperand.isStatic
    override val _value = if (isStatic) evaluate() else null
    override val value: Boolean
        get() = _value ?: evaluate()
    override fun evaluate(): Boolean = compare_operator.compare(rightOperand, leftOperand)
}



