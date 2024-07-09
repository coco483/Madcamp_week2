package com.example.madcamp_week2

import UserDataHolder
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week2.Class.Stock
import com.example.madcamp_week2.Class.StrategyReturn
import com.example.madcamp_week2.CodeBlock.ActionBlock
import com.example.madcamp_week2.MyLanguage.Action
import com.example.madcamp_week2.MyLanguage.Strategy
import com.example.madcamp_week2.databinding.FragmentStrategyAddBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StrategyAddFragment: Fragment() {
    private var _binding: FragmentStrategyAddBinding? = null
    private val binding get() = _binding!!
    private var actionBlockList: MutableList<ActionBlock> = mutableListOf()
    private var StrategyPos: Int? = null
    private var startDate:String = ""
    private var endDate:String = ""
    private var initialCash: Int = 1000000
    private var greatestReturnRate: Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            StrategyPos = it.getInt("STRATEGY_POS")
        }
        StrategyPos?.let {
            for (action in UserDataHolder.strategyList[it].actionList) {
                val newActionBlock = ActionBlock()
                newActionBlock.setAllChild(action)
                actionBlockList.add(newActionBlock)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStrategyAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anim = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
        binding.root.startAnimation(anim)

        val recyclerView = binding.fragmentStrategyAddActionRV
        val adapter = ActionBlockAdapter(actionBlockList, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        //set default startDate and endDate
        val cal = Calendar.getInstance()
        startDate = "${"%04d".format(cal.get(Calendar.YEAR) -1)}${"%02d".format(cal.get(Calendar.MONTH))}${"%02d".format(cal.get(Calendar.DAY_OF_MONTH))}"
        endDate = "${"%04d".format(cal.get(Calendar.YEAR))}${"%02d".format(cal.get(Calendar.MONTH))}${"%02d".format(cal.get(Calendar.DAY_OF_MONTH))}"
        binding.fragmentStrategyAddStartDateTV.text = "시작: $startDate"
        binding.fragmentStrategyAddEndDateTV.text = "종료: $endDate"

        binding.fragmentStrategyAddStartDateTV.setOnClickListener {
            getDateInput { dateStr ->
                startDate = dateStr
                binding.fragmentStrategyAddStartDateTV.text = "시작: $dateStr" }
        }
        binding.fragmentStrategyAddEndDateTV.setOnClickListener {
            getDateInput { dateStr ->
                if (dateStr.toInt() < startDate.toInt()) {
                    Toast.makeText(context, "시작일은 종료일보다 이후의 날짜여야 합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    endDate = dateStr
                    binding.fragmentStrategyAddEndDateTV.text = "종료: $dateStr" }
                }

        }
        binding.fragmentStrategyAddInitialCashTV.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_edittext_num, null)
            val editText = dialogView.findViewById<EditText>(R.id.dialog_ET)
            AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialog)
                .setTitle("초기 투자금")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                    val inputCash = editText.text.toString().toInt()
                    initialCash = inputCash * 10000
                    binding.fragmentStrategyAddInitialCashTV.text = "투자금 ${inputCash}만원"
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
        binding.fragmentStrategyAddAddActionBTN.setOnClickListener {
            val newActionBlock = ActionBlock()
            adapter.addAction(newActionBlock)
        }
        binding.fragmentStrategyAddCalculateBTN.setOnClickListener {
            if (actionBlockList.isEmpty()) Toast.makeText(requireContext(), "1개 이상의 조건 거래가 필요합니다", Toast.LENGTH_SHORT).show()
            else{
                val actionList:MutableList<Action> = mutableListOf()
                val relatedStockIdList: MutableList<Stock> = mutableListOf()
                for (actionBlock in actionBlockList){
                    val action = actionBlock.getAction(requireContext())
                    if (action != null) {
                        actionList.add(action)
                        relatedStockIdList += action.involvedStockList
                    } else {
                        return@setOnClickListener
                    }
                }
                val strategy = Strategy("title", relatedStockIdList, actionList)
                Log.d("StrategyCalculate", "$startDate, $endDate, $initialCash, ${Strategy}")
                val strategyReturn = strategy.calculate(startDate, endDate, initialCash)
                greatestReturnRate = strategyReturn.returnRate
                strategy.greatestReturnRate = greatestReturnRate
                if (StrategyPos != null) UserDataHolder.updateStrategy(strategy, StrategyPos!!)
                openShowReturnFragment(strategyReturn)
                updateUserToServer()
            }
        }
        binding.fragmentStrategyAddSaveBTN.setOnClickListener {
            var title = ""
            // gather actions and related Stocks, save in UserDataHolder
            if (actionBlockList.isEmpty()) Toast.makeText(requireContext(), "1개 이상의 조건 거래가 필요합니다", Toast.LENGTH_SHORT).show()
            else{
                val actionList:MutableList<Action> = mutableListOf()
                val relatedStockIdList: MutableList<Stock> = mutableListOf()
                for (actionBlock in actionBlockList){
                    val action = actionBlock.getAction(requireContext())
                    if (action != null) {
                        actionList.add(action)
                        relatedStockIdList += action.involvedStockList
                    } else return@setOnClickListener
                }
                val dialogView = layoutInflater.inflate(R.layout.dialog_edittext, null)
                val editText = dialogView.findViewById<EditText>(R.id.dialog_ET)
                // get user input for strategy title
                AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialog)
                    .setTitle("새로운 거래 전략의 이름")
                    .setView(dialogView)
                    .setPositiveButton("OK") { _, _ ->
                        title = editText.text.toString()
                        val strategy = Strategy(title, relatedStockIdList, actionList, greatestReturnRate)
                        if (StrategyPos != null) UserDataHolder.updateStrategy(strategy, StrategyPos!!)
                        else UserDataHolder.addStrategy(strategy)
                        updateUserToServer()
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show()
            }
            // post to ServerDB

        }

    }

    private fun getDateInput(callback: (String) -> Unit) {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            val dateStr = "${"%04d".format(year)}${"%02d".format(month + 1)}${"%02d".format(day)}" // month + 1 because months are zero-based
            callback(dateStr)
        }
        DatePickerDialog(requireContext(), R.style.AppCompatAlertDialog, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateUserToServer(){
        val user = UserDataHolder.getUser()
        if (user == null) {
            Toast.makeText(context, "유저정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        ApiClient.apiService.updateUserData(user.id, user).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    context?.let {
                        Toast.makeText(context, "서버에 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMessage = "Failed to update strategy list: ${response.message()}"
                    context?.let {
                        Log.e("StrategySavePost", errorMessage)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val error = "Error: ${t.message}"
                context?.let {
                    Log.e("StrategyAddFragment", error, t)
                    Toast.makeText(context, "네트워크 문제로 서버 저장에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun openShowReturnFragment(strategyReturn: StrategyReturn) {
        val showReturnFragment = ShowReturnFragment().apply {
            arguments = Bundle().apply {
                putDouble("RETURN_RATE", strategyReturn.returnRate)
                putInt("CASH_AMOUNT", strategyReturn.cashAmount)
                val keys = mutableListOf<String>()
                val values = mutableListOf<String>()
                for ((key, value) in strategyReturn.stockHoldMap) {
                    keys.add(key)
                    values.add(String.format("%.2f", value))
                }
                putStringArray("STOCK_NAME", keys.toTypedArray())
                putStringArray("STOCK_AMOUNT", values.toTypedArray())
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(com.example.madcamp_week2.R.id.blank_container, showReturnFragment)
            .addToBackStack(null)
            .commit()
    }

}