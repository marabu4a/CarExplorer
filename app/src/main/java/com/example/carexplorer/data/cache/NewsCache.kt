package com.example.carexplorer.data.cache

import com.example.carexplorer.data.model.enities.News
import kotlinx.coroutines.flow.Flow

interface NewsCache {
    fun getNews() : Flow<List<News>>
    fun getNewsByTitle(title: String) : News?
    fun removeNewsByTitle(title: String)
    fun saveNews(news: News)
}