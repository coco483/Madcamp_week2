package com.example.madcamp_week2.MyLanguage

import android.util.Log
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

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

enum class FloatOperator(val calculate: (Float, Float) -> Float) {
    ADD({ x, y -> x + y }),
    SUB({ x, y -> x - y }),
    MUL({ x, y -> x * y }),
    DIV({ x, y -> x / y });
}

@Serializable
sealed interface MyFloat {
    abstract fun evaluate(stockPriceMap: Map<String, stockData>): Float

    @Serializable
    data class MyFloatBinaryOp(
        private val rightOperand: MyFloat,
        private val leftOperand: MyFloat,
        private val floatOperator: FloatOperator
    ) : MyFloat {
        override fun evaluate(stockPriceMap: Map<String, stockData>): Float =
            floatOperator.calculate(
                rightOperand.evaluate(stockPriceMap),
                leftOperand.evaluate(stockPriceMap)
            )
    }

    @Serializable
    data class MyStockPrice(
        val stockID: String
    ) : MyFloat {
        override fun evaluate(stockPriceMap: Map<String, stockData>): Float {
            Log.d("MyInt", "stockprice is: ${stockPriceMap[stockID]}")
            return stockPriceMap[stockID]?.stck_clpr?.toFloat() ?: 0f
        }
    }

    @Serializable
    data class MyNum(
        private val num: Float
    ) : MyFloat {
        override fun evaluate(stockPriceMap: Map<String, stockData>): Float = num
    }


}