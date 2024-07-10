package com.example.madcamp_week2

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun dateOneMonthBefore(dateStr: String): String {
    // Define the formatter for the input date string
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    // Parse the input date string into a LocalDate object
    val date = LocalDate.parse(dateStr, formatter)

    // Subtract 6 months from the date
    val sixMonthsBefore = date.minus(1, ChronoUnit.MONTHS)

    // Format the new date back into a string
    return sixMonthsBefore.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun dateSixMonthsBefore(dateStr: String): String {
    // Define the formatter for the input date string
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    // Parse the input date string into a LocalDate object
    val date = LocalDate.parse(dateStr, formatter)

    // Subtract 6 months from the date
    val sixMonthsBefore = date.minus(6, ChronoUnit.MONTHS)

    // Format the new date back into a string
    return sixMonthsBefore.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun dateOneYearBefore(dateStr: String): String {
    // Define the formatter for the input date string
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    // Parse the input date string into a LocalDate object
    val date = LocalDate.parse(dateStr, formatter)

    // Subtract 1 year from the date
    val oneYearBefore = date.minus(1, ChronoUnit.YEARS)

    // Format the new date back into a string
    return oneYearBefore.format(formatter)
}
