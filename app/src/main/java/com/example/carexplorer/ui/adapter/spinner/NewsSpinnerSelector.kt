package com.example.carexplorer.ui.adapter.spinner

sealed class NewsSpinnerSelector(val type: String) {
    class RandomNews(type: String) : NewsSpinnerSelector(type)
    class RecentNews(type: String) : NewsSpinnerSelector(type)
}


enum class NewsSpinnerSelectorType {
    RANDOM, RECENT
}