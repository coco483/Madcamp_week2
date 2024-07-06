package com.example.madcamp_week2

import android.app.Application

class StockSimApp : Application() {
    override fun onCreate() {
        super.onCreate()
        StockDataHolder.initialize(this)
    }
}