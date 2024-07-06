package com.example.madcamp_week2

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.madcamp_week2.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tab1 = StockSearchFragment()

        supportFragmentManager.beginTransaction().replace(R.id.blank_container, tab1).commit()
        binding.bottomNavigation.setOnItemSelectedListener (
            object: NavigationBarView.OnItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    var selectedFragment: Fragment?= null
                    when(item.itemId) {
                        R.id.tab1 -> selectedFragment = StockSearchFragment()
                        R.id.tab2 -> selectedFragment = StockSearchFragment()
                        R.id.tab3 -> selectedFragment = StockSearchFragment()
                    }
                    selectedFragment?.let {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.blank_container, selectedFragment)
                            .commit()
                        return true
                    }
                    return false
                }
            }
        )
    }
}