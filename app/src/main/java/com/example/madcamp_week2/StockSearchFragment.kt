package com.example.madcamp_week2

import FavoriteAdapter
import User
import UserDataHolder
import UserDataHolder.favoriteList
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
//        val favoriteList = UserDataHolder.favoriteList
        val favoriteNameList = UserDataHolder.favoriteList.mapNotNull { stock ->
            stock.name
        } // 여기 나중에 바꾸기

        Log.d("StockSearchFragment", "Favorite list: $favoriteNameList")

        // Create adapter and set it to RecyclerView
        val adapter = FavoriteAdapter(favoriteList)
        binding.stockSearchFavoriteRV.layoutManager = LinearLayoutManager(requireContext())
        binding.stockSearchFavoriteRV.adapter = adapter

        adapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener {
            override fun onCardViewClick(view: View, stock: Stock, pos: Int) {
                // Handle click event as needed
                openStockDetailFragment(stock)
            }
        })
    }

    private fun openStockDetailFragment(stock: Stock) {
        val stockDetailFragment = StockDetailFragment().apply {
            arguments = Bundle().apply {
                putString("STOCK_ID", stock.id)
                putString("STOCK_NAME", stock.name)
                putString("STOCK_MARKET", stock.market)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(com.example.madcamp_week2.R.id.blank_container, stockDetailFragment)
            .addToBackStack(null)
            .commit()
    }
}
