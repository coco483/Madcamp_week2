package com.example.madcamp_week2

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun getThreeMonthsBefore(inputDate: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val date = LocalDate.parse(inputDate, formatter)
    val oneMonthBefore = date.minus(3, ChronoUnit.MONTHS)
    return oneMonthBefore.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getOneYearBefore(inputDate: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val date = LocalDate.parse(inputDate, formatter)
    val oneMonthBefore = date.minus(1, ChronoUnit.YEARS)
    return oneMonthBefore.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getFiveYearsBefore(inputDate: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val date = LocalDate.parse(inputDate, formatter)
    val oneMonthBefore = date.minus(5, ChronoUnit.YEARS)
    return oneMonthBefore.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDateInFormat(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    return currentDate.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDayEarlier(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val date = LocalDate.parse(dateString, formatter)
    val dayEarlier = date.minusDays(1)
    return dayEarlier.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getWeekEarlier(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val date = LocalDate.parse(dateString, formatter)
    val dayEarlier = date.minusDays(7)
    return dayEarlier.format(formatter)
}