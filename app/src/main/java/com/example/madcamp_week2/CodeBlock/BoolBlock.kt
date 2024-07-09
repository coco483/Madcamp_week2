package com.example.madcamp_week2.CodeBlock

import android.content.Context
import android.widget.Toast
import com.example.madcamp_week2.Class.Stock
import com.example.madcamp_week2.MyLanguage.BoolOperator
import com.example.madcamp_week2.MyLanguage.CompareOperator
import com.example.madcamp_week2.MyLanguage.FloatOperator
import com.example.madcamp_week2.MyLanguage.MyBool
import com.example.madcamp_week2.MyLanguage.MyFloat
import com.example.madcamp_week2.MyLanguage.TradePlan

abstract class BoolBlock {
    abstract fun getMyBool(context: Context) : Pair<MyBool, List<Stock>>?
}

class CompareBlock: BoolBlock() {
    var rightOpBlock: FloatBlock? = null
    var leftOpBlock: FloatBlock? = null
    var comparator: CompareOperator? = null
    override fun getMyBool(context: Context): Pair<MyBool, List<Stock>>? {
        if (rightOpBlock == null) {
            Toast.makeText(context, "please fill in the right operand", Toast.LENGTH_LONG).show()
        } else if (leftOpBlock == null){
            Toast.makeText(context, "please fill in the left operand", Toast.LENGTH_LONG).show()
        } else if (comparator == null){
            Toast.makeText(context, "please choose in the comparator", Toast.LENGTH_LONG).show()
        } else {
            val (rightOperand, list1) = rightOpBlock!!.getMyFloat(context) ?: Pair(null, listOf())
            val (leftOperand, list2) = leftOpBlock!!.getMyFloat(context) ?: Pair(null, listOf())
            if (rightOperand != null && leftOperand != null) {
                return Pair(MyBool.MyCompare(rightOperand, leftOperand, comparator!!), (list1+list2))
            }
        }
        return null
    }
    fun setAllChild(myCompare: MyBool.MyCompare) {
        rightOpBlock = setAllChildOfFloat(myCompare.rightOperand)
        leftOpBlock = setAllChildOfFloat(myCompare.leftOperand)
        comparator = myCompare.comparator
    }
}

class BoolBinaryOpBlock : BoolBlock(){
    var rightOpBlock: BoolBlock? = null
    var leftOpBlock: BoolBlock? = null
    var boolOperator: BoolOperator? = null
    override fun getMyBool(context: Context): Pair<MyBool, List<Stock>>? {
        if (rightOpBlock == null) {
            Toast.makeText(context, "please fill in the right operand", Toast.LENGTH_LONG).show()
        } else if (leftOpBlock == null){
            Toast.makeText(context, "please fill in the left operand", Toast.LENGTH_LONG).show()
        } else if (boolOperator == null){
            Toast.makeText(context, "please choose the operator", Toast.LENGTH_LONG).show()
        } else {
            val (rightOperand, list1) = rightOpBlock!!.getMyBool(context) ?: Pair(null, listOf())
            val (leftOperand, list2) = leftOpBlock!!.getMyBool(context) ?: Pair(null, listOf())
            if (rightOperand != null && leftOperand != null && boolOperator != null) {
                return Pair(MyBool.MyBoolBinaryOp(rightOperand, leftOperand, boolOperator!!), (list1+list2))
            }
        }
        return null
    }
    fun setAllChild(myBoolBinaryOp: MyBool.MyBoolBinaryOp) {
        rightOpBlock = setAllChildOfBool(myBoolBinaryOp.rightOperand)
        leftOpBlock = setAllChildOfBool(myBoolBinaryOp.leftOperand)
        boolOperator = myBoolBinaryOp.boolOperator
    }
}

class NotBlock: BoolBlock(){
    var operandBlock: BoolBlock? = null
    override fun getMyBool(context: Context): Pair<MyBool, List<Stock>>? {
        if (operandBlock == null){
            Toast.makeText(context, "please fill in the operand", Toast.LENGTH_LONG).show()
        } else {
            val (operand, list) = operandBlock!!.getMyBool(context) ?: Pair(null, listOf())
            if(operand != null) {
                return Pair(MyBool.MyNot(operand), list)
            }
        }
        return null
    }
    fun setAllChild(myNot: MyBool.MyNot) {
        operandBlock = setAllChildOfBool(myNot.operand)
    }
}

