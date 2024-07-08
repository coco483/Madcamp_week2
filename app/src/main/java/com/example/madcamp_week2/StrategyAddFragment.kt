package com.example.madcamp_week2

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
import com.example.madcamp_week2.BlockLayout.addActionLayout
import com.example.madcamp_week2.BlockLayout.addCompareBlock
import com.example.madcamp_week2.CodeBlock.ActionBlock
import com.example.madcamp_week2.MyLanguage.Action
import com.example.madcamp_week2.MyLanguage.Strategy
import com.example.madcamp_week2.databinding.BlockActionBinding
import com.example.madcamp_week2.databinding.FragmentStrategyAddBinding
import com.example.madcamp_week2.databinding.FragmetStockSearchBinding

class StrategyAddFragment: Fragment() {
    private var _binding: FragmentStrategyAddBinding? = null
    private val binding get() = _binding!!
    private val actionBlockList: MutableList<ActionBlock> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStrategyAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                var str = StrategyToJson(strategy)
                Log.d("StockAdd", "json: $str")
                val newStrategy = JsonToStrategy(str)
                Log.d("StockAdd", "strategy: $newStrategy")
                Log.d("StockAdd", "back to json: ${StrategyToJson(newStrategy)}")
                str += "수익률: ${strategy.calculate("20240101", "20240707", 10000000)}%"

                binding.textView2.text = str
            }

        }

    }
}