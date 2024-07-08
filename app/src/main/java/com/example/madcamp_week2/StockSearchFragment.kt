package com.example.madcamp_week2

import FavoriteAdapter
import User
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
        // Initialize RecyclerView with LinearLayoutManager
        binding.stockSearchFavoriteRV.layoutManager = LinearLayoutManager(requireContext())

        // Get favorite list from FavoriteHolder
        val favoriteList = FavoriteHolder.favoriteList

        // Fetch user from server and compare IDs
        val userId = UserDataHolder.getUser()?.id ?: return

        ApiClient.apiService.getUserById(userId).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val serverUser = response.body()
                    Log.d("StockSearchFragment", "Fetched User ID: ${serverUser?.id}")
                    if (serverUser?.id == userId) {
                        // IDs match, update favorite list on the server
                        val favoriteListJson = Gson().toJson(favoriteList)
                        val updatedUser = User(serverUser.id, serverUser.name, favoriteListJson) // Update user with new favoriteList

                        ApiClient.apiService.updateUser(userId, updatedUser).enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if (response.isSuccessful) {
                                    // Ensure the context is not null
                                    context?.let {
                                        Toast.makeText(it, "User updated successfully", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    val errorMessage = "Failed to update user: ${response.message()}"
                                    context?.let {
                                        Toast.makeText(it, errorMessage, Toast.LENGTH_SHORT).show()
                                        Log.e("StockSearchFragment", errorMessage)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                val error = "Error: ${t.message}"
                                context?.let {
                                    Toast.makeText(it, error, Toast.LENGTH_SHORT).show()
                                    Log.e("StockSearchFragment", error, t)
                                }
                            }
                        })
                    } else {
                        // Handle ID mismatch case
                        context?.let {
                            Toast.makeText(it, "User ID mismatch", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Handle error case for fetching user
                    val errorMessage = "Failed to fetch user: ${response.message()}"
                    context?.let {
                        Toast.makeText(it, errorMessage, Toast.LENGTH_SHORT).show()
                        Log.e("StockSearchFragment", errorMessage)
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                val error = "Error: ${t.message}"
                context?.let {
                    Toast.makeText(it, error, Toast.LENGTH_SHORT).show()
                    Log.e("StockSearchFragment", error, t)
                }
            }
        })


        // Create adapter and set it to RecyclerView
        val adapter = FavoriteAdapter(favoriteList)
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
