package com.example.madcamp_week2.MyLanguage

abstract class MyCompareOperator {
    abstract fun compare(rightOp: MyInt, leftOp: MyInt): Boolean
}

class BT: MyCompareOperator(){
    override fun compare(rightOp: MyInt, leftOp: MyInt): Boolean {
        return rightOp.value > leftOp.value
    }
}

class LT: MyCompareOperator(){
    override fun compare(rightOp: MyInt, leftOp: MyInt): Boolean {
        return rightOp.value < leftOp.value
    }
}

class BTE: MyCompareOperator(){
    override fun compare(rightOp: MyInt, leftOp: MyInt): Boolean {
        return rightOp.value >= leftOp.value
    }
}

class LTE: MyCompareOperator(){
    override fun compare(rightOp: MyInt, leftOp: MyInt): Boolean {
        return rightOp.value <= leftOp.value
    }
}

class ET: MyCompareOperator(){
    override fun compare(rightOp: MyInt, leftOp: MyInt): Boolean {
        return rightOp.value == leftOp.value
    }
}
