package com.example.madcamp_week2

import android.app.VoiceInteractor
import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

data class Outputs(val output1: Output1, val output2: List<DailyData>)
data class Output1(val hts_kor_isnm: String)
data class DailyData(val stck_bsop_date: String, val stck_clpr: Double)

fun getHistoryData(stockCode: String, startDate: String, endDate: String, period: Char): String? {
    if ( period !in listOf('D', 'W', 'M', 'Y')) {
        Log.d("StockSearch", "$period is not a valid input")
        return null
    }
    val client = OkHttpClient()
    val dailyPriceUrl = buildString {
        append("https://openapivts.koreainvestment.com:29443/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice")
        append("?fid_cond_mrkt_div_code=J")
        append("&fid_input_iscd=$stockCode")
        append("&fid_input_date_1=$startDate")
        append("&fid_input_date_2=$endDate")
        append("&fid_period_div_code=$period")
        append("&fid_org_adj_prc=1")
    }
    val request = Request.Builder()
        .url(dailyPriceUrl)
        .addHeader("content-type", "application/json")
        .addHeader("authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2tlbiIsImF1ZCI6Ijg2OTBjMjM4LTc5OTctNDE2OC1hMjEwLWEyYjU5OWU4Njc3MCIsInByZHRfY2QiOiIiLCJpc3MiOiJ1bm9ndyIsImV4cCI6MTcyMDI2NTI4OSwiaWF0IjoxNzIwMTc4ODg5LCJqdGkiOiJQU0NBTUNwUm5qSW81MkF0Y29veTNLU1REVkd4emx2TFhLTWcifQ.y9INT0Yca1nJ5nwCVAqou2Vz4u15y03TOnYJLU_Z-PXKa2ovI574JjtNs-GrNRZDfQs127XNpIcxbfs6pnZGgA")
        .addHeader("appkey", "PSCAMCpRnjIo52Atcooy3KSTDVGxzlvLXKMg")
        .addHeader("appsecret", "aJ4WQxVfByuf5WWoO5IeRFvphNJWBYJpq00zvobmtmx6w9n7CdxRj9mbK13S3F343wjI26kT3yvwcpozDPY5Hx3qH6ODmNuEUqoDbgGZ1seuwWSeT5X8bd/HkiqqN8kFlHVk1TlYkm1U5MXIqxAtnITJGSti7WEm9ggKTC3UY6zQDQ9oUAs=")
        .addHeader("tr_id", "FHKST03010100")
        .build()
    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            Log.d("StockSearch", "Error: Failed to fetch data with status code ${response.code()}")
        }
        return response.body()?.string()
    }
}

fun parseHistoryData(responseStr:String): List<DailyData>{
    val stockData = Gson().fromJson(responseStr, Outputs::class.java)
    return stockData.output2
}
