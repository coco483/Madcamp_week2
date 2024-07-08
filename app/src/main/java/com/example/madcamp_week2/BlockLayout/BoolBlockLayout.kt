package com.example.madcamp_week2.BlockLayout

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.madcamp_week2.CodeBlock.BoolBinaryOpBlock
import com.example.madcamp_week2.CodeBlock.CompareBlock
import com.example.madcamp_week2.CodeBlock.FloatBinaryOpBlock
import com.example.madcamp_week2.CodeBlock.NotBlock
import com.example.madcamp_week2.MyLanguage.BoolOperator
import com.example.madcamp_week2.MyLanguage.CompareOperator
import com.example.madcamp_week2.MyLanguage.FloatOperator
import com.example.madcamp_week2.MyLanguage.MyBoolBinaryOp
import com.example.madcamp_week2.databinding.BlockBinaryBoolBinding
import com.example.madcamp_week2.databinding.BlockBinaryFloatBinding
import com.example.madcamp_week2.databinding.BlockNotBinding
import kotlin.contracts.contract

fun addCompareBlock(parentLayout: ViewGroup, context: Context): CompareBlock {
    return addBlock(parentLayout, context, BlockBinaryBoolBinding::inflate) { binding ->
        val block = CompareBlock()
        binding.blockBinaryBoolOperator.text = "<"
        block.comparator = CompareOperator.GT
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
            "<" to CompareOperator.GT,
            "<=" to CompareOperator.GTE,
            ">" to CompareOperator.LT,
            ">=" to CompareOperator.LTE)
        binding.blockBinaryBoolOperator.setOnClickListener {

            showRadioDialog(context, "choose operator", compareOperatorMap.keys.toList()) { op ->
                binding.blockBinaryBoolOperator.text = op
                block.comparator = compareOperatorMap[op]
            }
        }
        block
    }
}

fun addBoolBinaryOpBlock(parentLayout: ViewGroup, context: Context): BoolBinaryOpBlock {
    return addBlock(parentLayout, context, BlockBinaryBoolBinding::inflate) { binding ->
        val block = BoolBinaryOpBlock()
        binding.blockBinaryBoolOperator.text = "and"
        block.boolOperator = BoolOperator.AND
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
        block
    }
}

fun addNotBlock(parentLayout: ViewGroup, context: Context): NotBlock {
    return addBlock(parentLayout, context, BlockNotBinding::inflate) { binding ->
        val block = NotBlock()
        binding.blockNotOperand.setOnClickListener {
            showRadioDialog(context, "choose right operand", boolBlockFunctionList.keys.toList()) { i ->
                block.operandBlock =
                    boolBlockFunctionList[i]?.let { it1 -> it1(binding.blockNotOperand, context) }
            }
        }
        block
    }
}