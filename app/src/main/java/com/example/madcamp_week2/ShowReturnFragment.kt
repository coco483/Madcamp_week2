package com.example.madcamp_week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import com.example.madcamp_week2.databinding.FragmentShowReturnBinding
import com.example.madcamp_week2.databinding.FragmentStockDetailBinding
import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer
import kotlin.math.absoluteValue
import java.text.DecimalFormat

class ShowReturnFragment: Fragment() {
    private var _binding: FragmentShowReturnBinding? = null
    private val binding get() = _binding!!

    private var returnRate: Double? = null
    private var cashAmount: Int? = null
    private var stockNameArray: Array<String>? =null
    private var stocklAmountArray: Array<String>? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            returnRate = it.getDouble("RETURN_RATE")
            cashAmount = it.getInt("CASH_AMOUNT")
            stockNameArray = it.getStringArray("STOCK_NAME")
            stocklAmountArray = it.getStringArray("STOCK_AMOUNT")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowReturnBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var formattedReturnRate = ""
        val df = DecimalFormat("#.00")
        if (returnRate!! >= 0.0) {
            formattedReturnRate = "+"
            binding.fragmentShowReturnReturnRateTV.setTextColor(getResources().getColor(R.color.red))
        }
        else {
            binding.fragmentShowReturnReturnRateTV.setTextColor(getResources().getColor(R.color.blue))
        }
        formattedReturnRate += (String.format("%.3f", returnRate) + "%")

        binding.fragmentShowReturnReturnRateTV.text = formattedReturnRate
        val nameStr = stockNameArray!!.joinToString(prefix = "현금\n", separator = "\n")
        val formattedCashAmount = cashAmount!!/10000
        var amountStr = ""
        if (stocklAmountArray!!.isEmpty()) amountStr = "${formattedCashAmount}만원\n"
        else amountStr = stocklAmountArray!!.joinToString(prefix = "${formattedCashAmount}만원\n", separator = "주\n", postfix = "주")
        binding.fragmentShowReturnCapitalNameListTV.text = nameStr
        binding.fragmentShowReturnCapitalAmountListTV.text = amountStr
    }
}