package com.example.carexplorer.repository.cache

import com.example.carexplorer.data.model.enities.News

interface NewsCache {
    fun getNews() : List<News>
    fun getNewsByTitle(title: String) : News?
    fun removeNewsByTitle(title: String)
    fun saveNews(news: News)
}