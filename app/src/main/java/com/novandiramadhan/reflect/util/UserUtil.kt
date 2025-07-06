package com.novandiramadhan.reflect.util

import com.google.firebase.Timestamp

fun parseTimestamp(timestampStr: String): Timestamp? {
    return try {
        val parts = timestampStr.split(":")
        if (parts.size == 2) {
            val seconds = parts[0].toLong()
            val nanoseconds = parts[1].toInt()
            Timestamp(seconds, nanoseconds)
        } else null
    } catch (e: Exception) {
        null
    }
}

fun formatTimestamp(timestamp: Timestamp): String {
    return "${timestamp.seconds}:${timestamp.nanoseconds}"
}