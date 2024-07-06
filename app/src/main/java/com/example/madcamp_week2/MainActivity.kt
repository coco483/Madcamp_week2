package com.example.madcamp_week2

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var itemsTextView: TextView
    private var stockDataSeries =  LineGraphSeries<DataPoint>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var dailyStockData: List<DailyData>? = null
        itemsTextView = findViewById(R.id.itemsTextView)
        val loadPriceThread = Thread {
            val dailyStockJson = getHistoryData("005930", "20230702", "20240704", 'D')
            dailyStockData = dailyStockJson?.let { parseHistoryData(it) }
        }
        loadPriceThread.start()
        loadPriceThread.join()


        val graphView = findViewById<GraphView>(R.id.stock_detail_graph_GV)

        var see_text = ""
        dailyStockData = dailyStockData?.reversed()
        if (dailyStockData==null){
            Toast.makeText(baseContext, "failed to get data", Toast.LENGTH_LONG).show()
        } else {
            val num_stockData = dailyStockData!!.size
            var day_count = 0.0
            for (dailyData in dailyStockData!!){
                val x = day_count
                day_count += 1.0
                val y = dailyData.stck_clpr
                see_text += "date: ${dailyData.stck_bsop_date}, price: $y\n"
                stockDataSeries.appendData(DataPoint(x, y), true, num_stockData+100)
            }
        }
        graphView.addSeries(stockDataSeries)
        graphView.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    // show normal x values
                    val index = value.toInt()
                    dailyStockData?.get(index)?.stck_bsop_date ?: "00000000"
                } else {
                    // show currency for y values
                    super.formatLabel(value, isValueX) + "원"
                }
            }
        }
        graphView.getGridLabelRenderer().setHorizontalLabelsAngle(135)
        graphView.getViewport().setScrollable(true)
        itemsTextView.text = see_text


    }

    private fun fetchItems() {
        val call = ApiClient.apiService.getToData()

        call.enqueue(object : Callback<List<TodoData>> {
            override fun onResponse(call: Call<List<TodoData>>, response: Response<List<TodoData>>) {
                if (response.isSuccessful) {
                    val items = response.body()!!
                    displayItems(items)
                } else {
                    Log.e("MainActivity", "Request failed with response code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<TodoData>>, t: Throwable) {
                Log.e("MainActivity", "Network request failed", t)
            }
        })
    }

    private fun addItems(newTodo: TodoData) {
        val call = ApiClient.apiService.postToDoData(newTodo)
        call.enqueue(object : Callback<TodoData> {
            override fun onResponse(call: Call<TodoData>, response: Response<TodoData>) {
                if (response.isSuccessful) {
                    Toast.makeText(baseContext, "successful add: ${newTodo.toString()}", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("MainActivity", "Request failed with response code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<TodoData>, t: Throwable) {
                Log.e("MainActivity", "Network request failed", t)
            }
        })
    }

    private fun <T> displayItems(items: List<T>) {
        val stringBuilder = StringBuilder()
        for (item in items) {
            stringBuilder.append(item.toString() + "\n")
        }
        itemsTextView.text = stringBuilder.toString()
    }
}