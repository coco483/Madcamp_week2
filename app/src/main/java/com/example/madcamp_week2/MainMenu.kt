package com.example.madcamp_week2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.madcamp_week2.databinding.ActivityMainMenuBinding

class MainMenu : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize buttons
        val button1 = binding.button1
        val button2 = binding.button2
        val button3 = binding.button3

        // Set click listeners for each button
        button1.setOnClickListener {
            navigateToFragment("frag1")
        }

        button2.setOnClickListener {
            navigateToFragment("frag2")
        }

        button3.setOnClickListener {
            navigateToFragment("frag3")
        }
    }

    private fun navigateToFragment(fragmentName: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("fragment", fragmentName)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // No specific handling needed here
    }
}
