package com.example.madcamp_week2

import FavoriteAdapter
import User
import UserDataHolder
import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week2.databinding.FragmetStockSearchBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockSearchFragment: Fragment() {
    private var _binding: FragmetStockSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmetStockSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchAdapter = StockDataHolder.stockList?.let {
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
        }
        binding.stockSearchSearchInputACTV.setAdapter(searchAdapter)
        binding.stockSearchSearchInputACTV.setOnItemClickListener { adapterView, view, i, l ->
            val selectedStock = adapterView.getItemAtPosition(i) as Stock
            openStockDetailFragment(selectedStock)
        }

        // Get favorite list from FavoriteHolder
        val favoriteList = UserDataHolder.favoriteList
        Log.d("StockSearchFragment", "Favorite list: $favoriteList")

        // Create adapter and set it to RecyclerView
        val adapter = FavoriteAdapter(favoriteList)
        binding.stockSearchFavoriteRV.layoutManager = LinearLayoutManager(requireContext())
        binding.stockSearchFavoriteRV.adapter = adapter

        adapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener {
            override fun onCardViewClick(view: View, favoriteItem: String, pos: Int) {
                showDialog(favoriteItem)
            }
        })
    }

    fun showDialog(stockId: String) {
        val stock = StockDataHolder.stockList?.find { it.id == stockId }

        if (stock != null) {
            val stockName = stock.name
            val dialogBuilder = AlertDialog.Builder(requireContext())

            dialogBuilder.setTitle("즐겨찾기 상세 정보")
            dialogBuilder.setMessage("주식 ID: $stockId\n주식 이름: $stockName")
            dialogBuilder.setPositiveButton("확인") { dialog, _ ->
                // 확인 버튼 클릭 시 처리할 로직 (예: 다이얼로그 닫기)
                dialog.dismiss()
            }

            val dialog = dialogBuilder.create()
            dialog.show()
        } else {
            // 주식 객체를 찾을 수 없는 경우에 대한 처리
        }
    }

    private fun openStockDetailFragment(stock: Stock) {
        val stockDetailFragment = StockDetailFragment().apply {
            arguments = Bundle().apply {
                putString("STOCK_ID", stock.id)
                putString("STOCK_NAME", stock.name)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(com.example.madcamp_week2.R.id.blank_container, stockDetailFragment)
            .addToBackStack(null)
            .commit()
    }
}
