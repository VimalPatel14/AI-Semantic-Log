package com.vimal.theaisemanticlog.core.util

import com.vimal.theaisemanticlog.core.util.DateTimeConstants.API_TIMESTAMP_FORMAT
import com.vimal.theaisemanticlog.core.util.DateTimeConstants.DISPLAY_TIMESTAMP_FORMAT
import com.vimal.theaisemanticlog.core.util.DateTimeConstants.UTC_TIMEZONE
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun String.formatLogTimestamp(): String {
    return try {
        val inputFormatter = SimpleDateFormat(
            API_TIMESTAMP_FORMAT,
            Locale.US
        ).apply {
            timeZone = TimeZone.getTimeZone(UTC_TIMEZONE)
        }
        val outputFormatter = SimpleDateFormat(DISPLAY_TIMESTAMP_FORMAT, Locale.US)

        inputFormatter.parse(this)
            ?.let { date ->
                outputFormatter.format(date)
            } ?: this
    } catch (e: Exception) {
        this
    }
}