package com.example.madcamp_week2

import UserDataHolder
import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week2.BlockLayout.addCompareBlock
import com.example.madcamp_week2.CodeBlock.ActionBlock
import com.example.madcamp_week2.MyLanguage.Action
import com.example.madcamp_week2.MyLanguage.Strategy
import com.example.madcamp_week2.databinding.BlockActionBinding
import com.example.madcamp_week2.databinding.FragmentStrategyAddBinding
import com.example.madcamp_week2.databinding.FragmetStockSearchBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StrategyAddFragment: Fragment() {
    private var _binding: FragmentStrategyAddBinding? = null
    private val binding get() = _binding!!
    private var actionBlockList: MutableList<ActionBlock> = mutableListOf()
    private var StrategyPos: Int? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "StrategyPos: $StrategyPos", Toast.LENGTH_LONG).show()

        val recyclerView = binding.fragmentStrategyAddActionRV
        val adapter = ActionBlockAdapter(actionBlockList, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        binding.fragmentStrategyAddAddActionBTN.setOnClickListener {
            val newActionBlock = ActionBlock()
            //actionBlockList.add(newActionBlock)
            adapter.addAction(newActionBlock)
        }
        binding.fragmentStrategyAddCalculateBTN.setOnClickListener {
            if (actionBlockList.isEmpty()) Toast.makeText(requireContext(), "no action!", Toast.LENGTH_SHORT).show()
            else{
                val actionList:MutableList<Action> = mutableListOf()
                val relatedStockIdList: MutableList<String> = mutableListOf()
                for (actionBlock in actionBlockList){
                    val action = actionBlock.getAction(requireContext())
                    if (action != null) {
                        actionList.add(action)
                        relatedStockIdList += action.involvedStockIdList
                    } else return@setOnClickListener
                }
                val strategy = Strategy("title", relatedStockIdList, actionList)
                Log.d("StockAdd", "strategy: $strategy")


                var str = strategyListToJson(listOf( strategy))
                str += "수익률: ${strategy.calculate("20240101", "20240707", 10000000)}%"
                Log.d("StockAdd", "json: $str")
                binding.textView2.text = str
            }
        }

        binding.fragmentStrategyAddSaveBTN.setOnClickListener {
            // store to UserDataHolder
            if (actionBlockList.isEmpty()) Toast.makeText(requireContext(), "no action!", Toast.LENGTH_SHORT).show()
            else{
                val actionList:MutableList<Action> = mutableListOf()
                val relatedStockIdList: MutableList<String> = mutableListOf()
                for (actionBlock in actionBlockList){
                    val action = actionBlock.getAction(requireContext())
                    if (action != null) {
                        actionList.add(action)
                        relatedStockIdList += action.involvedStockIdList
                    } else return@setOnClickListener
                }
                val strategy = Strategy("title", relatedStockIdList, actionList)
                if (StrategyPos != null) UserDataHolder.updateStrategy(strategy, StrategyPos!!)
                else UserDataHolder.addStrategy(strategy)
            }
            // post to ServerDB
            val user = UserDataHolder.getUser()
            if (user == null) {
                Toast.makeText(context, "com.example.madcamp_week2.Class.User data is not available", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            ApiClient.apiService.updateUserData(user.id, user).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        context?.let {
                            Toast.makeText(it, "added to strategy list", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorMessage = "Failed to update strategy list: ${response.message()}"
                        context?.let {
                            Log.e("StrategyAddFragment", errorMessage)
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    val error = "Error: ${t.message}"
                    context?.let {
                        Log.e("StrategyAddFragment", error, t)
                    }
                }
            })
        }

    }
}