package com.example.carexplorer.helpers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carexplorer.data.model.CachedArticle

class NewsViewModel : ViewModel() {
    private lateinit var news : MutableLiveData<List<CachedArticle>>


    fun getNewsList() : MutableLiveData<List<CachedArticle>> {
        if (!::news.isInitialized) {
            news = MutableLiveData()
        }
        return news
    }

    fun changeDataNews(list : List<CachedArticle>) {
        if (!::news.isInitialized) {
            news = MutableLiveData()
        }
        news.value = list
    }
}