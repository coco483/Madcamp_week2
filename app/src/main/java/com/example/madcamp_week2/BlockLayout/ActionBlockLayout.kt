package com.example.madcamp_week2.BlockLayout

import android.R
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.viewbinding.ViewBinding
import com.example.madcamp_week2.CodeBlock.ActionBlock
import com.example.madcamp_week2.CodeBlock.CompareBlock
import com.example.madcamp_week2.CodeBlock.TradePlanBlock
import com.example.madcamp_week2.MyLanguage.CompareOperator
import com.example.madcamp_week2.MyLanguage.TradeType
import com.example.madcamp_week2.Stock
import com.example.madcamp_week2.StockDataHolder
import com.example.madcamp_week2.databinding.BlockActionBinding
import com.example.madcamp_week2.databinding.BlockTradePlanBinding

inline fun <reified T, B : ViewBinding> addBlock(
    parentLayout: ViewGroup,
    context: Context,
    inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> B,
    setupBlock: (B) -> T
): T {
    val inflater = LayoutInflater.from(context)
    val binding = inflateBinding(inflater, parentLayout, false)
    val block = setupBlock(binding)
    parentLayout.addView(binding.root)
    return block
}
fun addActionLayout(parentLayout: ViewGroup, context: Context): ActionBlock {
    return addBlock(parentLayout, context, BlockActionBinding::inflate) { binding ->
        val block = ActionBlock()
        binding.blockActionCondition.setOnClickListener {
             showRadioDialog(context, "choose condition for trade", boolBlockFunctionList.keys.toList()) { i ->
                 block.conditionBlock =
                     boolBlockFunctionList[i]?.let { it1 -> it1(binding.blockActionCondition, context) }
        }
        }
        binding.blockActionTradePlan.setOnClickListener {
            block.tradePlanBlock = addTradePlanLayout(binding.blockActionTradePlan, context)
        }
        block
    }
}


fun addTradePlanLayout(parentLayout: ViewGroup, context: Context): TradePlanBlock {
    return addBlock(parentLayout, context, BlockTradePlanBinding::inflate) { binding ->
        val block = TradePlanBlock()
        block.tradeType = TradeType.BUY
        binding.blockTradePlanTradeType.setOnCheckedChangeListener { _, isChecked ->
            block.tradeType = if (isChecked) TradeType.SELL else TradeType.BUY
        }
        binding.blockTradePlanStockAmount.setOnClickListener {
            block.amountBlock = addNumBlock(binding.blockTradePlanStockAmount, context)
        }
        val searchAdapter = StockDataHolder
            .stockList?.let { ArrayAdapter(context, R.layout.simple_list_item_1, it) }
        binding.blockTradePlanStockIDACTV.setAdapter(searchAdapter)
        binding.blockTradePlanStockIDACTV.setOnItemClickListener { adapterView, view, i, l ->
            val selectedStock = adapterView.getItemAtPosition(i) as Stock
            block.stockId = selectedStock.id
        }
        block
    }
}

