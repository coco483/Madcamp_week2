package com.example.madcamp_week2

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun getOneMonthBefore(inputDate: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val date = LocalDate.parse(inputDate, formatter)
    val oneMonthBefore = date.minus(1, ChronoUnit.MONTHS)
    return oneMonthBefore.format(formatter)
}