package com.example.cakecrave.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    fun formatFullDateTime(timestamp: Long): String {
        if (timestamp <= 0L) return "N/A"

        val formatter =
            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

        return formatter.format(Date(timestamp))
    }
}
