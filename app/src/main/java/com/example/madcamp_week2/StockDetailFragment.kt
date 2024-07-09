package com.example.madcamp_week2

import UserDataHolder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.madcamp_week2.Class.Stock
import com.example.madcamp_week2.Class.User
import com.example.madcamp_week2.databinding.FragmentStockDetailBinding
import com.google.gson.Gson
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockDetailFragment : Fragment() {
    private var _binding: FragmentStockDetailBinding? = null
    private val binding get() = _binding!!
    private var stockDataSeries = LineGraphSeries<DataPoint>()

    private lateinit var stockId: String
    private lateinit var stockName: String
    private lateinit var stockMarket: String

    private var isFavorite = false // Track whether the stock is in favorites

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            stockId = it.getString("STOCK_ID").toString()
            stockName = it.getString("STOCK_NAME").toString()
            stockMarket = it.getString("STOCK_MARKET").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStockDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the stock name and market to the respective TextViews
        binding.stockDetailNameTV.text = stockName
        binding.stockDetailMarketTV.text = stockMarket

        // Check if the stock is already in favorites
        isFavorite = UserDataHolder.isFavorite(Stock(stockId, stockName, stockMarket))

        // Update favorite button text based on initial state
        updateFavoriteButton()

        // Reload graph with 'D' period
        reloadGraph(binding.stockDetailGraphGV, 'D')

        // Add to favorites button click listener
        binding.stockFavoriteBT.setOnClickListener {
            if (isFavorite) {
                // Remove from favorites
                UserDataHolder.removeFavorite(Stock(stockId, stockName, stockMarket))
                Toast.makeText(context, "Removed from favorite list", Toast.LENGTH_SHORT).show()
                val user = UserDataHolder.getUser()
                Log.d("StockDetailFragment", "User after removal: $user")
            } else {
                // Add to favorites
                UserDataHolder.addFavorite(Stock(stockId, stockName, stockMarket))
                Toast.makeText(context, "Added to favorite list", Toast.LENGTH_SHORT).show()
                val user = UserDataHolder.getUser()
                Log.d("StockDetailFragment", "User after addal: $user")
            }
            // Toggle favorite state
            isFavorite = !isFavorite
            // Update favorite button text
            updateFavoriteButton()

            // Fetch user from server and compare IDs
            val user = UserDataHolder.getUser()
            if (user == null) {
                Toast.makeText(context, "User data is not available", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // getUser gives user with updated Favorite List and Strategy List
            ApiClient.apiService.updateUserData(user.id, user).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        context?.let {
                            Toast.makeText(it, "Added to favorite list", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorMessage = "Failed to update favorite list: ${response.message()}"
                        context?.let {
                            Log.e("StockDetailFragment", errorMessage)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    val error = "Error: ${t.message}"
                    context?.let {
                        Log.e("StockDetailFragment", error, t)
                    }
                }
            })
        }

        // Close button click listener
        binding.stockCloseBT.setOnClickListener {
            // Navigate back to SearchFragment
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun updateFavoriteButton() {
        if (isFavorite) {
            binding.stockFavoriteBT.text = "즐겨찾기 제거"
        } else {
            binding.stockFavoriteBT.text = "즐겨찾기 추가"
        }
        Log.d("StockDetailFragment", "Favorite button text updated. isFavorite: $isFavorite")
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun reloadGraph(graph: GraphView, period: Char) {
        var dailyStockData = getHistoryData(stockId, "20160101", "20240706", period)

        dailyStockData = dailyStockData.reversed()
        if (dailyStockData != null) {
            addStockDataToGraph(graph, dailyStockData)
            formatGraphLabel(graph, dailyStockData)
            customizeGraphView(graph)
        }
    }

    private fun addStockDataToGraph(graph: GraphView, stockDatas: List<stockData>) {
        val maxDataPoints = stockDatas.size
        var dayCount = 0.0
        for (stockData in stockDatas) {
            val x = dayCount
            dayCount += 1.0
            val y = stockData.stck_clpr
            stockDataSeries.appendData(DataPoint(x, y), false, maxDataPoints)
        }
        graph.addSeries(stockDataSeries)
    }

    private fun formatGraphLabel(graph: GraphView, stockDatas: List<stockData>) {
        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    // show normal x values
                    val index = value.toInt()
                    stockDatas[index].stck_bsop_date
                } else {
                    // show currency for y values
                    super.formatLabel(value, isValueX) + "원"
                }
            }
        }
        graph.gridLabelRenderer.setHorizontalLabelsAngle(150)
        graph.viewport.isScrollable = true
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(99.0)
    }

    private fun customizeGraphView(graph: GraphView) {
        // Get the GridLabelRenderer from the GraphView
        val gridLabelRenderer = graph.gridLabelRenderer

        // Hide Y axis labels
        gridLabelRenderer.isVerticalLabelsVisible = false
        gridLabelRenderer.isHorizontalLabelsVisible = false

        // Hide grid lines
        gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.NONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
