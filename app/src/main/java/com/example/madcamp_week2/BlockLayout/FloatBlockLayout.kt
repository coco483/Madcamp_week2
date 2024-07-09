package com.example.madcamp_week2.BlockLayout

import android.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.madcamp_week2.CodeBlock.FloatBinaryOpBlock
import com.example.madcamp_week2.CodeBlock.NumBlock
import com.example.madcamp_week2.CodeBlock.StockPriceBlock
import com.example.madcamp_week2.MyLanguage.FloatOperator
import com.example.madcamp_week2.Class.Stock
import com.example.madcamp_week2.StockDataHolder
import com.example.madcamp_week2.databinding.BlockBinaryFloatBinding
import com.example.madcamp_week2.databinding.BlockNumBinding
import com.example.madcamp_week2.databinding.BlockStockPriceBinding



fun addNumBlock(parentLayout: ViewGroup, context: Context, block: NumBlock) {
    return addBlock(parentLayout, context, BlockNumBinding::inflate, block) { binding ->
        if(block.num != null) binding.numBlockNumET.setText(block.num.toString())
        binding.numBlockNumET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                block.num = s.toString().toFloat()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

    }
}

fun addStockPriceBlock(parentLayout: ViewGroup, context: Context, block: StockPriceBlock) {
    return addBlock(parentLayout, context, BlockStockPriceBinding::inflate, block) { binding ->
        if (block.stock != null) binding.blockStockPriceACTV.setText(block.stock!!.name)
        val searchAdapter = StockDataHolder
            .stockList?.let { ArrayAdapter(context, R.layout.simple_list_item_1, it) }
        binding.blockStockPriceACTV.setAdapter(searchAdapter)
        binding.blockStockPriceACTV.setOnItemClickListener { adapterView, view, i, l ->
            val selectedStock = adapterView.getItemAtPosition(i) as Stock
            block.stock = selectedStock
        }
    }
}
fun addFloatBinaryOpBlock(parentLayout: ViewGroup, context: Context, block: FloatBinaryOpBlock) {
    return addBlock(parentLayout, context, BlockBinaryFloatBinding::inflate, block) { binding ->
        setFloatBlock(binding.blockBinaryFloatLeftOp, context, block.leftOpBlock)
        setFloatBlock(binding.blockBinaryFloatRightOp, context, block.rightOpBlock)
        setFloatOperator(binding.blockBinaryFloatOperator, block)

        binding.blockBinaryFloatRightOp.setOnClickListener {
            showRadioDialog(context, "choose right operand", floatBlockFuntionList.keys.toList()) { i ->
                block.rightOpBlock =
                    floatBlockFuntionList[i]?.let { it1 -> it1(binding.blockBinaryFloatRightOp, context) }
            }
        }
        binding.blockBinaryFloatLeftOp.setOnClickListener {
            showRadioDialog(context, "choose left operand", floatBlockFuntionList.keys.toList()) { i ->
                block.leftOpBlock =
                    floatBlockFuntionList[i]?.let { it1 -> it1(binding.blockBinaryFloatLeftOp, context) }
            }
        }
        val floatOperatorMap = mapOf(
            "+" to FloatOperator.ADD,
            "-" to FloatOperator.SUB,
            "*" to FloatOperator.MUL,
            "/" to FloatOperator.DIV)
        binding.blockBinaryFloatOperator.setOnClickListener {
            showRadioDialog(context, "choose operator", floatOperatorMap.keys.toList()) { op ->
                block.floatOperator = floatOperatorMap[op]
                setFloatOperator(binding.blockBinaryFloatOperator, block)
            }
        }
    }
}

fun setFloatOperator(parentLayout: TextView, block: FloatBinaryOpBlock){
    parentLayout.text = when(block.floatOperator) {
        FloatOperator.ADD -> "+"
        FloatOperator.SUB -> "-"
        FloatOperator.MUL -> "*"
        FloatOperator.DIV -> "/"
        null -> {
            block.floatOperator = FloatOperator.ADD
            "+"
        }
    }
}