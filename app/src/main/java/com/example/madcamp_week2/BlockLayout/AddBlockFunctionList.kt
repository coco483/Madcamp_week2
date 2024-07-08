package com.example.madcamp_week2.BlockLayout

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.madcamp_week2.CodeBlock.BoolBinaryOpBlock
import com.example.madcamp_week2.CodeBlock.BoolBlock
import com.example.madcamp_week2.CodeBlock.CompareBlock
import com.example.madcamp_week2.CodeBlock.FloatBinaryOpBlock
import com.example.madcamp_week2.CodeBlock.FloatBlock
import com.example.madcamp_week2.CodeBlock.NotBlock
import com.example.madcamp_week2.CodeBlock.NumBlock
import com.example.madcamp_week2.CodeBlock.StockPriceBlock
import com.example.madcamp_week2.CodeBlock.TradePlanBlock
import java.lang.NumberFormatException

val floatBlockFuntionList: Map<String, (ViewGroup, Context) -> FloatBlock> = mapOf(
    "num" to { x, y ->
        val block = NumBlock()
        addNumBlock(x, y, block)
        block},
    "stock price" to { x, y ->
        val block = StockPriceBlock()
        addStockPriceBlock(x, y, block)
        block},
    "arithmetic" to {x, y ->
        val block = FloatBinaryOpBlock()
        addFloatBinaryOpBlock(x, y, block)
        block}
)

val boolBlockFunctionList: Map<String, (ViewGroup, Context) -> BoolBlock> = mapOf(
    "compare" to { x, y ->
        val block = CompareBlock()
        addCompareBlock(x, y, block)
                 block},
    "not" to { x, y ->
        val block = NotBlock()
        addNotBlock(x, y, block)
        block},
    "logical operation" to { x, y ->
        val block = BoolBinaryOpBlock()
        addBoolBinaryOpBlock(x, y, block)
        block}
)

fun setFloatBlock(parentLayout: ViewGroup, context: Context, floatBlock: FloatBlock?){
    when (floatBlock) {
        is FloatBinaryOpBlock -> addFloatBinaryOpBlock(parentLayout, context, floatBlock)
        is NumBlock -> addNumBlock(parentLayout, context, floatBlock)
        is StockPriceBlock -> addStockPriceBlock(parentLayout, context, floatBlock)
    }
}

fun setBoolBlock(parentLayout: ViewGroup, context: Context, boolBlock: BoolBlock?){
    when (boolBlock) {
        is CompareBlock -> addCompareBlock(parentLayout, context, boolBlock)
        is BoolBinaryOpBlock -> addBoolBinaryOpBlock(parentLayout, context, boolBlock)
        is NotBlock -> addNotBlock(parentLayout, context, boolBlock)
    }
}

fun setTradeTypeBlock(parentLayout: ViewGroup, context: Context, tradePlanBlock: TradePlanBlock?){
    tradePlanBlock?.let { addTradePlanLayout(parentLayout, context, it) }
}

fun showRadioDialog(context: Context, title: String, options: List<String>, onItemSelected: (String) -> Unit) {
    var selectedOption = options[0] // Default selected option
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setSingleChoiceItems(options.toTypedArray(), 0) { _, which ->
        selectedOption = options[which]
    }
    builder.setPositiveButton("OK") { dialog, _ ->
        onItemSelected(selectedOption)
        dialog.dismiss()
    }
    builder.setNegativeButton("Cancel") { dialog, _ ->
        dialog.cancel()
    }
    val dialog = builder.create()
    dialog.show()
}