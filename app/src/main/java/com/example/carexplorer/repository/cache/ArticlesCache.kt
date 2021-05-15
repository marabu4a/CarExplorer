package com.example.carexplorer.repository.cache

import com.example.carexplorer.data.model.enities.Article

interface ArticlesCache {
    fun getArticles(): List<Article>
    fun getArticleByTitle(title : String) : Article?
    fun removeArticleByTitle(title:String)
    fun saveArticle(article : Article)
}
