package com.example.madcamp_week2.BlockLayout

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.example.madcamp_week2.CodeBlock.ActionBlock
import com.example.madcamp_week2.CodeBlock.TradePlanBlock
import com.example.madcamp_week2.MyLanguage.TradeType
import com.example.madcamp_week2.Class.Stock
import com.example.madcamp_week2.CodeBlock.NumBlock
import com.example.madcamp_week2.StockDataHolder
import com.example.madcamp_week2.databinding.BlockActionBinding
import com.example.madcamp_week2.databinding.BlockTradePlanBinding

inline fun <reified T, B : ViewBinding> addBlock(
    parentLayout: ViewGroup,
    context: Context,
    inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> B,
    block: T,
    setupBlock: (B) -> Unit
) {
    val inflater = LayoutInflater.from(context)
    val binding = inflateBinding(inflater, parentLayout, false)
    setupBlock(binding)
    parentLayout.addView(binding.root)
}


fun addTradePlanLayout(parentLayout: ViewGroup, context: Context, block:TradePlanBlock) {
    return addBlock(parentLayout, context, BlockTradePlanBinding::inflate, block) { binding ->
        // set child layout if child blocks are already set
        if (block.tradeType == TradeType.SELL) binding.blockTradePlanTradeType.isSelected = true
        else block.tradeType = TradeType.BUY
        setFloatBlock(binding.blockTradePlanStockAmount, context, block.amountBlock)
        if (block.stock != null) binding.blockTradePlanStockIDACTV.setText(block.stock!!.name)
        // set child layout by user input
        binding.blockTradePlanTradeType.setOnCheckedChangeListener { _, isChecked ->
            block.tradeType = if (isChecked) TradeType.SELL else TradeType.BUY
        }
        binding.blockTradePlanStockAmount.setOnClickListener {
            showRadioDialog(context, "거래 수량", floatBlockFuntionList.keys.toList()) { i ->
                block.amountBlock =
                    floatBlockFuntionList[i]?.let { it1 -> it1(binding.blockTradePlanStockAmount, context) }
            }
        }
        val searchAdapter = StockDataHolder
            .stockList?.let { ArrayAdapter(context, R.layout.simple_list_item_1, it) }
        binding.blockTradePlanStockIDACTV.setAdapter(searchAdapter)
        binding.blockTradePlanStockIDACTV.setOnItemClickListener { adapterView, view, i, l ->
            val selectedStock = adapterView.getItemAtPosition(i) as Stock
            block.stock = selectedStock
        }
    }
}




