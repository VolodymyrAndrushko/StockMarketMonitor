package com.vandrushko.core.data.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatTimestampToHHMMSS(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}

fun formatTimestampToHHMM(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}
fun formatTimestampToHHmmSSddMMM(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm:ss, dd MMM", Locale.getDefault())
    return sdf.format(Date(timestamp))
}