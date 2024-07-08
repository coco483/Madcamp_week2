package com.example.madcamp_week2.BlockLayout

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.madcamp_week2.CodeBlock.BoolBinaryOpBlock
import com.example.madcamp_week2.CodeBlock.BoolBlock
import com.example.madcamp_week2.CodeBlock.CompareBlock
import com.example.madcamp_week2.CodeBlock.FloatBinaryOpBlock
import com.example.madcamp_week2.CodeBlock.NotBlock
import com.example.madcamp_week2.MyLanguage.BoolOperator
import com.example.madcamp_week2.MyLanguage.CompareOperator
import com.example.madcamp_week2.databinding.BlockBinaryBoolBinding
import com.example.madcamp_week2.databinding.BlockBinaryFloatBinding
import com.example.madcamp_week2.databinding.BlockNotBinding
import kotlin.contracts.contract

fun addCompareBlock(parentLayout: ViewGroup, context: Context, block: CompareBlock) {
    return addBlock(parentLayout, context, BlockBinaryBoolBinding::inflate, block) { binding ->
        setFloatBlock(binding.blockBinaryBoolLeftOp, context, block.leftOpBlock)
        setFloatBlock(binding.blockBinaryBoolRightOp, context, block.rightOpBlock)
        setCompareOperator(binding.blockBinaryBoolOperator, block)

        binding.blockBinaryBoolRightOp.setOnClickListener {
            showRadioDialog(context, "choose right operand", floatBlockFuntionList.keys.toList()) { i ->
                block.rightOpBlock =
                    floatBlockFuntionList[i]?.let { it1 -> it1(binding.blockBinaryBoolRightOp, context) }
            }
        }
        binding.blockBinaryBoolLeftOp.setOnClickListener {
            showRadioDialog(context, "choose left operand", floatBlockFuntionList.keys.toList()) { i ->
                block.leftOpBlock =
                    floatBlockFuntionList[i]?.let { it1 -> it1(binding.blockBinaryBoolLeftOp, context) }
            }
        }
        val compareOperatorMap = mapOf(
            ">" to CompareOperator.GT,
            ">=" to CompareOperator.GTE,
            "<" to CompareOperator.LT,
            "<=" to CompareOperator.LTE)
        binding.blockBinaryBoolOperator.setOnClickListener {

            showRadioDialog(context, "choose operator", compareOperatorMap.keys.toList()) { op ->
                block.comparator = compareOperatorMap[op]
                setCompareOperator(binding.blockBinaryBoolOperator, block)
            }
        }
    }
}

fun setCompareOperator(layout:TextView, block: CompareBlock){
    val str = when (block.comparator){
        CompareOperator.GT -> ">"
        CompareOperator.LT -> "<"
        CompareOperator.GTE -> ">="
        CompareOperator.LTE -> "<="
        null -> {
            block.comparator = CompareOperator.GT
            ">"
        }
    }
    layout.setText(str)
}
fun addBoolBinaryOpBlock(parentLayout: ViewGroup, context: Context, block: BoolBinaryOpBlock) {
    return addBlock(parentLayout, context, BlockBinaryBoolBinding::inflate, block) { binding ->
        setBoolBlock(binding.blockBinaryBoolLeftOp, context, block.leftOpBlock)
        setBoolBlock(binding.blockBinaryBoolRightOp, context, block.rightOpBlock)
        setBoolOperator(binding.blockBinaryBoolOperator, block)
        binding.blockBinaryBoolRightOp.setOnClickListener {
            showRadioDialog(context, "choose right operand", boolBlockFunctionList.keys.toList()) { i ->
                block.rightOpBlock =
                    boolBlockFunctionList[i]?.let { it1 -> it1(binding.blockBinaryBoolRightOp, context) }
            }
        }
        binding.blockBinaryBoolLeftOp.setOnClickListener {
            showRadioDialog(context, "choose left operand", boolBlockFunctionList.keys.toList()) { i ->
                block.leftOpBlock =
                    boolBlockFunctionList[i]?.let { it1 -> it1(binding.blockBinaryBoolLeftOp, context) }
            }
        }
        val boolOperatorMap = mapOf(
            "and" to BoolOperator.AND,
            "or" to BoolOperator.OR )
        binding.blockBinaryBoolOperator.setOnClickListener {
            showRadioDialog(context, "choose operator", boolOperatorMap.keys.toList()) { op ->
                binding.blockBinaryBoolOperator.text = op
                block.boolOperator = boolOperatorMap[op]
            }
        }
    }
}

fun setBoolOperator(layout:TextView, block: BoolBinaryOpBlock){
    val str = when (block.boolOperator){
        BoolOperator.AND -> "and"
        BoolOperator.OR -> "or"
        null -> {
            block.boolOperator = BoolOperator.AND
            "and"
        }
    }
    layout.setText(str)
}

fun addNotBlock(parentLayout: ViewGroup, context: Context, block: NotBlock) {
    return addBlock(parentLayout, context, BlockNotBinding::inflate, block) { binding ->
        setBoolBlock(binding.blockNotOperand, context, block.operandBlock)
        binding.blockNotOperand.setOnClickListener {
            showRadioDialog(context, "choose right operand", boolBlockFunctionList.keys.toList()) { i ->
                block.operandBlock =
                    boolBlockFunctionList[i]?.let { it1 -> it1(binding.blockNotOperand, context) }
            }
        }
    }
}