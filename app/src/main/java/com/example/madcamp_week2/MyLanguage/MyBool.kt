package com.example.madcamp_week2.MyLanguage

import com.example.madcamp_week2.MyLanguage.MyFloat.MyFloatBinaryOp
import com.example.madcamp_week2.MyLanguage.MyFloat.MyNum
import com.example.madcamp_week2.MyLanguage.MyFloat.MyStockPrice
import com.example.madcamp_week2.stockData
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule

enum class BoolOperator (val calculate: (Boolean, Boolean) -> Boolean) {
    AND({ x, y -> x && y }),
    OR({ x, y -> x || y }),
}
enum class CompareOperator(val compare: (Float, Float) -> Boolean) {
    GT({ x, y -> x > y }),
    LT({ x, y -> x < y }),
    GTE({ x, y -> x >= y }),
    LTE({ x, y -> x <= y });
}

@Serializable
sealed interface MyBool {
    abstract fun evaluate(stockPriceMap: Map<String, stockData>): Boolean


    @Serializable
    data class MyBoolBinaryOp(
        val rightOperand: MyBool,
        val leftOperand: MyBool,
        val boolOperator: BoolOperator
    ) : MyBool {
        override fun evaluate(stockPriceMap: Map<String, stockData>): Boolean =
            boolOperator.calculate(
                leftOperand.evaluate(stockPriceMap),
                rightOperand.evaluate(stockPriceMap)
            )
    }

    @Serializable
    data class MyNot(
        val operand: MyBool,
    ) : MyBool {
        override fun evaluate(stockPriceMap: Map<String, stockData>): Boolean =
            !(operand.evaluate(stockPriceMap))
    }


    @Serializable
    data class MyCompare(
        val rightOperand: MyFloat,
        val leftOperand: MyFloat,
        val comparator: CompareOperator
    ) : MyBool {
        override fun evaluate(stockPriceMap: Map<String, stockData>): Boolean = comparator.compare(
            leftOperand.evaluate(stockPriceMap),
            rightOperand.evaluate(stockPriceMap)
        )
    }


}
