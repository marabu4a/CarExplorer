package com.example.carexplorer.helpers

import com.example.carexplorer.data.model.enities.News
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun List<News>.convertDates() = this.map { it.copy(date = it.date.convertDateTime()) }

fun String.convertDateTime() = LocalDateTime.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")).toString()