package com.example.madcamp_week2.CodeBlock

import com.example.madcamp_week2.MyLanguage.MyBool
import com.example.madcamp_week2.MyLanguage.MyFloat

fun setAllChildOfBool(myBool: MyBool): BoolBlock {
    val block: BoolBlock = when (myBool) {
        is MyBool.MyBoolBinaryOp -> BoolBinaryOpBlock().apply { setAllChild(myBool) }
        is MyBool.MyCompare -> CompareBlock().apply { setAllChild(myBool) }
        is MyBool.MyNot -> NotBlock().apply { setAllChild(myBool) }
    }
    return block
}

fun setAllChildOfFloat(myFloat: MyFloat): FloatBlock {
    val block: FloatBlock = when (myFloat) {
        is MyFloat.MyFloatBinaryOp -> FloatBinaryOpBlock().apply { setAllChild(myFloat) }
        is MyFloat.MyNum -> NumBlock().apply { setAllChild(myFloat) }
        is MyFloat.MyStockPrice -> StockPriceBlock().apply { setAllChild(myFloat) }
    }
    return block
}