package com.example.madcamp_week2

import UserDataHolder
import android.os.Build
import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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


    private lateinit var stockId: String
    private lateinit var stockName: String
    private lateinit var stockMarket: String
    private val stockDataSeries = LineGraphSeries<DataPoint>()
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

        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left)
        binding.root.startAnimation(anim)

        // Set the stock name and market to the respective TextViews
        binding.stockDetailNameTV.text = stockName
        binding.stockDetailMarketTV.text = stockMarket

        // Check if the stock is already in favorites
        isFavorite = UserDataHolder.isFavorite(Stock(stockId, stockName, stockMarket))

        // Update favorite button text based on initial state
        updateFavoriteButton()

        // Reload graph with '3M' period by default
        reloadGraph(binding.stockDetailGraphGV, "3M")
        binding.stockDetailGraphGV.addSeries(stockDataSeries)
        // set period for graph
        binding.stockDetail3MBTN.setOnClickListener { reloadGraph(binding.stockDetailGraphGV, "3M") }
        binding.stockDetail1YBTN.setOnClickListener { reloadGraph(binding.stockDetailGraphGV, "1Y") }
        binding.stockDetail5YBTN.setOnClickListener { reloadGraph(binding.stockDetailGraphGV, "5Y") }

        // Add to favorites button click listener
        binding.stockFavoriteBT.setOnClickListener {
            if (isFavorite) {
                // Remove from favorites
                UserDataHolder.removeFavorite(Stock(stockId, stockName, stockMarket))
                Toast.makeText(context, "즐겨찾기 해제", Toast.LENGTH_SHORT).show()
                val user = UserDataHolder.getUser()
                Log.d("StockDetailFragment", "User after removal: $user")
            } else {
                // Add to favorites
                UserDataHolder.addFavorite(Stock(stockId, stockName, stockMarket))
                Toast.makeText(context, "즐겨찾기에 추가", Toast.LENGTH_SHORT).show()
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
                Log.d("StockDetail", "User data is not available")
                return@setOnClickListener
            }

            // getUser gives user with updated Favorite List and Strategy List
            ApiClient.apiService.updateUserData(user.id, user).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        context?.let {
                            Log.d("StockDetail", "Added to favorite list")
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
    private fun reloadGraph(graph: GraphView, period: String) {
        if ( period !in listOf("3M", "1Y", "5Y")) {
            Log.d("ReloadGraph", "$period is not a valid input")
            return
        }
        var interval = 'D'
        val today = getCurrentDateInFormat()
        var startDate = "20240710"
        var returnStr = ""
        when (period){
            "3M" -> {
                interval = 'D'
                returnStr = "세달 전보다 "
                startDate = getThreeMonthsBefore(startDate)
            }
            "1Y" -> {
                interval = 'W'
                returnStr = "1년 전보다 "
                startDate = getOneYearBefore(startDate)
            }
            "5Y" -> {
                interval = 'M'
                returnStr = "5년 전보다 "
                startDate = getFiveYearsBefore(startDate)
            }
        }
        var dailyStockData = getHistoryData(stockId, startDate, today, interval)

        dailyStockData = dailyStockData.reversed()
        replaceStockDataToGraph(graph, dailyStockData)
        customizeGraphView(graph, dailyStockData)

    }


    private fun replaceStockDataToGraph(graph: GraphView, stockDatas: List<stockData>) {
        var dayCount = 0.0
        val newDataPoints = mutableListOf<DataPoint>()
        for (stockData in stockDatas) {
            val x = dayCount
            dayCount += 1.0
            val y = stockData.stck_clpr
            newDataPoints.add(DataPoint(x,y))
            //stockDataSeries.appendData(DataPoint(x, y), false, maxDataPoints)
        }
        stockDataSeries.resetData(newDataPoints.toTypedArray())
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

    private fun customizeGraphView(graph: GraphView, stockDatas: List<stockData>) {
        val numDataPoints = stockDatas.size
        // Get the GridLabelRenderer from the GraphView
        val gridLabelRenderer = graph.gridLabelRenderer

        // Hide Y axis labels
        gridLabelRenderer.isVerticalLabelsVisible = false
        gridLabelRenderer.isHorizontalLabelsVisible = false

        // Hide grid lines
        gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.NONE

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(numDataPoints.toDouble())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
