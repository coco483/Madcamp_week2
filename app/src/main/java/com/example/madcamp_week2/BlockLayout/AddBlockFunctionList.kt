package com.example.madcamp_week2.BlockLayout

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.madcamp_week2.CodeBlock.BoolBlock
import com.example.madcamp_week2.CodeBlock.FloatBlock

val floatBlockFuntionList:Map<String, (ViewGroup, Context) -> FloatBlock> = mapOf(
    "num" to { x, y -> addNumBlock(x, y)},
    "stock price" to { x, y -> addStockPriceBlock(x, y) },
    "arithmetic" to {x, y -> addFloatBinaryOpBlock(x, y) }
)

val boolBlockFunctionList:Map<String, (ViewGroup, Context) -> BoolBlock> = mapOf(
    "compare" to { x, y -> addCompareBlock(x, y) },
    "not" to { x, y -> addNotBlock(x, y) },
    "logical operation" to { x, y -> addBoolBinaryOpBlock(x, y) }
)

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