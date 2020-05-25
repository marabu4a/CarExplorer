package com.example.carexplorer.repository.cache

import com.example.carexplorer.data.model.CachedArticle


interface ContentCache {
    fun getArticles(): List<CachedArticle>
    fun getArticleByTitle(title : String) : CachedArticle?
    fun removeArticleByTitle(title:String)
    fun saveArticle(article : CachedArticle)
}
