package com.almax.stopwatch.utils

import java.util.Locale

fun formatTime(time: Long): String {
    val seconds = (time / 1000) % 60
    val minutes = time / 1000 / 60
    val milliseconds = (time % 1000) / 10

    return String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, milliseconds)
}