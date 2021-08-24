package com.example.carexplorer.data.cache

import com.example.carexplorer.data.model.enities.Article
import kotlinx.coroutines.flow.Flow

interface ArticlesCache {
    fun getArticles(): Flow<List<Article>>
    fun getArticleByTitle(title : String) : Article?
    fun removeArticleByTitle(title:String)
    fun saveArticle(article : Article)
}
