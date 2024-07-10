package com.example.madcamp_week2

import android.app.VoiceInteractor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Thread.sleep
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Outputs(val output1: Output1, val output2: List<stockData>)
data class Output1(val hts_kor_isnm: String, val stck_oprc: String, val hts_avls: String)
data class stockData(val stck_bsop_date: String, val stck_clpr: Double)


@RequiresApi(Build.VERSION_CODES.O)
fun getHistoryData(stockCode: String, startDate: String, endDate: String, interval: Char): List<stockData> {
    val startDateAsInt = startDate.toInt()
    val dataList:MutableList<stockData> = mutableListOf()
    var nextEndDate = endDate
    while(startDateAsInt <= nextEndDate.toInt()){
        val jsonStr = requestStockData(stockCode, startDate, nextEndDate, interval)
        val outputs = jsonStr?.let { parseHistoryData(it) }
        if (outputs == null) {
            return dataList
        } else if (outputs.isEmpty() || outputs.first().stck_clpr == 0.0) return dataList
        dataList.addAll(outputs.filter { it.stck_clpr != 0.0 })
        Log.d("GetHistoryData", "start: $startDateAsInt, end: $nextEndDate, $outputs")
        nextEndDate = getDayEarlier(outputs.last().stck_bsop_date)

    }
    return dataList
}

@RequiresApi(Build.VERSION_CODES.O)
fun getOnedayPrice(stockCode: String): String{
    val jsonStr = requestStockData(stockCode, "20240709", "20240709", 'D')
    val stockData = Gson().fromJson(jsonStr, Outputs::class.java)
    return stockData.output1.stck_oprc
}

fun requestStockData(stockCode: String, startDate: String, endDate: String, interval: Char): String? {
    Log.d("ApiRequest", "stockCode: $stockCode, startDate: $startDate, endDate: $endDate")
    if ( interval !in listOf('D', 'W', 'M', 'Y')) {
        Log.d("StockSearch", "$interval is not a valid input")
        return null
    }
    val client = OkHttpClient()
    val dailyPriceUrl = buildString {
        append("https://openapivts.koreainvestment.com:29443/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice")
        append("?fid_cond_mrkt_div_code=J")
        append("&fid_input_iscd=$stockCode")
        append("&fid_input_date_1=$startDate")
        append("&fid_input_date_2=$endDate")
        append("&fid_period_div_code=$interval")
        append("&fid_org_adj_prc=1")
    }
    val request = Request.Builder()
        .url(dailyPriceUrl)
        .addHeader("content-type", "application/json")
        .addHeader("authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2tlbiIsImF1ZCI6IjkwMTAwM2IyLTlkNDAtNDJiMS05MWU2LWVhZDUzMzQ5Njk1MCIsInByZHRfY2QiOiIiLCJpc3MiOiJ1bm9ndyIsImV4cCI6MTcyMDY2ODIxNywiaWF0IjoxNzIwNTgxODE3LCJqdGkiOiJQU0NBTUNwUm5qSW81MkF0Y29veTNLU1REVkd4emx2TFhLTWcifQ.3g3WKJOnXBSmHJj--Zl8RsspXM5YsTV82eLZ-0o3d8z1RMNMFUDCR_wUeztSsstsG5vrwlaO4BWuq3TG3SL6Gg")
        .addHeader("appkey", "PSCAMCpRnjIo52Atcooy3KSTDVGxzlvLXKMg")
        .addHeader("appsecret", "aJ4WQxVfByuf5WWoO5IeRFvphNJWBYJpq00zvobmtmx6w9n7CdxRj9mbK13S3F343wjI26kT3yvwcpozDPY5Hx3qH6ODmNuEUqoDbgGZ1seuwWSeT5X8bd/HkiqqN8kFlHVk1TlYkm1U5MXIqxAtnITJGSti7WEm9ggKTC3UY6zQDQ9oUAs=")
        .addHeader("tr_id", "FHKST03010100")
        .build()
    var dailyStockJson: String? = null
    val loadPriceThread = Thread {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Log.d(
                    "StockSearch",
                    "Error: Failed to fetch data with status code ${response.code()}"
                )
            }
            dailyStockJson =  response.body()?.string()
        }
    }
    loadPriceThread.start()
    sleep(500)
    loadPriceThread.join()
    return dailyStockJson
}

fun parseHistoryData(responseStr:String): List<stockData>{
    val stockData = Gson().fromJson(responseStr, Outputs::class.java)
    Log.d("Parsing!", responseStr)
    return stockData.output2
}



