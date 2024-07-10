package com.example.madcamp_week2.Strategy

import StrategyAdapter
import UserDataHolder
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.StockDetailFragment
import com.example.madcamp_week2.StrategyAddFragment

class StrategyFragment : Fragment() {

    private lateinit var recyclerViewStrategy: RecyclerView
    private lateinit var addButton: Button
    private lateinit var strategyAdapter: StrategyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_strategy, container, false)

        recyclerViewStrategy = view.findViewById(R.id.strategy_content_RV)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anim = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
        view.startAnimation(anim)

        // RecyclerView 설정
        val blue = requireContext().getColor(R.color.blue)
        val red = requireContext().getColor(R.color.red)
        recyclerViewStrategy.layoutManager = LinearLayoutManager(requireContext())
        strategyAdapter = StrategyAdapter(red, blue, UserDataHolder.strategyList, setStrategyAddFragment) // 초기화면 빈 리스트로 시작
        recyclerViewStrategy.adapter = strategyAdapter

        // RecyclerView에 Divider 추가
        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        recyclerViewStrategy.addItemDecoration(dividerItemDecoration) // divider 넣기
    }

    val setStrategyAddFragment = { position:Int ->
        val strategyAddFragment = StrategyAddFragment().apply {
            arguments = Bundle().apply {
                putInt("STRATEGY_POS", position)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(com.example.madcamp_week2.R.id.blank_container, strategyAddFragment)
            .addToBackStack(null)
            .commit()
    }


}
