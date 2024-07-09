package com.example.madcamp_week2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.madcamp_week2.Strategy.StrategyFragment
import com.example.madcamp_week2.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initially load the fragment based on intent extras
        val fragmentName = intent.getStringExtra("fragment")
        val initialFragment = when (fragmentName) {
            "frag1" -> StockSearchFragment()
            "frag2" -> StrategyAddFragment()
            "frag3" -> StrategyFragment()
            else -> StockSearchFragment() // Default fragment, if needed
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.blank_container, initialFragment)
            .commit()

        // Set the bottom navigation's selected item based on the fragment
        when (fragmentName) {
            "frag1" -> binding.bottomNavigation.selectedItemId = R.id.tab1
            "frag2" -> binding.bottomNavigation.selectedItemId = R.id.tab2
            "frag3" -> binding.bottomNavigation.selectedItemId = R.id.tab3
        }

        // Setup bottom navigation listener
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.tab1 -> selectedFragment = StockSearchFragment()
                R.id.tab2 -> selectedFragment = StrategyAddFragment()
                R.id.tab3 -> selectedFragment = StrategyFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.blank_container, selectedFragment)
                    .commit()
                return@setOnItemSelectedListener true
            }
            return@setOnItemSelectedListener false
        }
    }
}
