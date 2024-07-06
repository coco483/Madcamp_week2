package com.example.madcamp_week2

import android.os.Bundle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.madcamp_week2.ui.theme.Madcamp_week2Theme
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.madcamp_week2.Strategy.StrategyFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var itemsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemsTextView = findViewById(R.id.itemsTextView)

        // 내가 추가
        val fragment = StrategyFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        // 내가 추가

        fetchItems()
    }

    private fun fetchItems() {
        val call = ApiClient.apiService.getItems()

        call.enqueue(object : Callback<List<TodoItem>> {
            override fun onResponse(call: Call<List<TodoItem>>, response: Response<List<TodoItem>>) {
                if (response.isSuccessful) {
                    val items = response.body()!!
                    displayItems(items)
                } else {
                    Log.e("MainActivity", "Request failed with response code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<TodoItem>>, t: Throwable) {
                Log.e("MainActivity", "Network request failed", t)
            }
        })
    }

    private fun displayItems(items: List<TodoItem>) {
        val stringBuilder = StringBuilder()
        for (item in items) {
            stringBuilder.append("title: ${item.title}\n")
            stringBuilder.append("completed: ${item.completed}\n\n")
            Log.d("DISPLAY", "title: ${item.title}, completed: ${item.completed}")
        }
        itemsTextView.text = stringBuilder.toString()
    }
}