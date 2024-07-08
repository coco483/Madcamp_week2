package com.example.madcamp_week2.BlockLayout

import android.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.madcamp_week2.CodeBlock.FloatBinaryOpBlock
import com.example.madcamp_week2.CodeBlock.NumBlock
import com.example.madcamp_week2.CodeBlock.StockPriceBlock
import com.example.madcamp_week2.MyLanguage.CompareOperator
import com.example.madcamp_week2.MyLanguage.FloatOperator
import com.example.madcamp_week2.Stock
import com.example.madcamp_week2.StockDataHolder
import com.example.madcamp_week2.databinding.BlockBinaryFloatBinding
import com.example.madcamp_week2.databinding.BlockNumBinding
import com.example.madcamp_week2.databinding.BlockStockPriceBinding



fun addNumBlock(parentLayout: ViewGroup, context: Context): NumBlock {
    return addBlock(parentLayout, context, BlockNumBinding::inflate) { binding ->
        val block = NumBlock()

        binding.numBlockNumET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                block.num = s.toString().toFloat()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        block

    }
}

fun addStockPriceBlock(parentLayout: ViewGroup, context: Context): StockPriceBlock {
    return addBlock(parentLayout, context, BlockStockPriceBinding::inflate) { binding ->
        val block = StockPriceBlock()
        val searchAdapter = StockDataHolder
            .stockList?.let { ArrayAdapter(context, R.layout.simple_list_item_1, it) }
        binding.blockStockPriceACTV.setAdapter(searchAdapter)
        binding.blockStockPriceACTV.setOnItemClickListener { adapterView, view, i, l ->
            val selectedStock = adapterView.getItemAtPosition(i) as Stock
            block.stockID = selectedStock.id
        }
        block
    }
}
fun addFloatBinaryOpBlock(parentLayout: ViewGroup, context: Context): FloatBinaryOpBlock {
    return addBlock(parentLayout, context, BlockBinaryFloatBinding::inflate) { binding ->
        val block = FloatBinaryOpBlock()
        binding.blockBinaryFloatOperator.text = "+"
        block.floatOperator = FloatOperator.ADD
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
                binding.blockBinaryFloatOperator.text = op
                block.floatOperator = floatOperatorMap[op]
            }
        }
        block
    }
}