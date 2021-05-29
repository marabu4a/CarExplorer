package com.example.carexplorer.helpers

import com.example.carexplorer.data.model.enities.News
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun List<News>.convertDates() = this.map { it.copy(date = it.date.convertDateTime()?.toString() ?: "--.--.---- --:--") }

fun String.convertDateTime(): String = try {
    val filtered = this.filter { !it.isLetter() || it != ','  }
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm", Locale.ENGLISH)).toString()
} catch (e: Exception) {
    "25.05.2021 18:00"
}

fun String.isNullOrNotEmpty() : String? = if (this.length < 5) null else this