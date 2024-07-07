package com.example.madcamp_week2.Strategy

import StrategyAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R

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
        addButton = view.findViewById(R.id.add_item_button)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        recyclerViewStrategy.layoutManager = LinearLayoutManager(requireContext())
        strategyAdapter = StrategyAdapter(mutableListOf()) // 초기화면 빈 리스트로 시작
        recyclerViewStrategy.adapter = strategyAdapter

        // RecyclerView에 Divider 추가
        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        recyclerViewStrategy.addItemDecoration(dividerItemDecoration) // divider 넣기


        // 아이템 추가 버튼 클릭 시 동작 설정
        addButton.setOnClickListener {
            // 새로운 전략 아이템 추가 로직
            val newStrategy = StrategyList("새로운 전략", listOf("태그1", "태그2")) // 예시 데이터
            strategyAdapter.addStrategyItem(newStrategy)
            recyclerViewStrategy.smoothScrollToPosition(strategyAdapter.itemCount - 1)
        }
    }
}