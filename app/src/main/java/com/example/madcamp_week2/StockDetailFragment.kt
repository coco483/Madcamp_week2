package com.example.madcamp_week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madcamp_week2.databinding.FragmentStockDetailBinding
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class StockDetailFragment: Fragment() {
    private var _binding: FragmentStockDetailBinding? = null
    private val binding get() = _binding!!
    private var stockDataSeries = LineGraphSeries<DataPoint>()

    private lateinit var stockId: String
    private lateinit var stockName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            stockId = it.getString("STOCK_ID").toString()
            stockName = it.getString("STOCK_NAME").toString()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStockDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reloadGraph(binding.stockDetailGraphGV, 'D')
    }

    private fun reloadGraph(graph: GraphView, period: Char){
        var dailyStockJson = getHistoryData(stockId, "20160101", "20240706", period)
        var dailyStockData = dailyStockJson?.let { parseHistoryData(it) }

        dailyStockData = dailyStockData?.reversed()
        if (dailyStockData != null) {
            addStockDataToGraph(graph, dailyStockData)
            formatGraphLabel(graph, dailyStockData)
        }
    }
    private fun addStockDataToGraph(graph: GraphView, stockDatas:List<stockData>){
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

    private fun formatGraphLabel(graph: GraphView, stockDatas: List<stockData>){
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
        graph.viewport.setXAxisBoundsManual(true)
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(99.0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}