package com.example.carexplorer.repository.cache

import androidx.room.*
import com.example.carexplorer.data.model.CachedArticle


@Dao
interface ContentDao : ContentCache {

    @Query("SELECT * FROM articles")
    override fun getArticles() : List<CachedArticle>

    @Query("SELECT * FROM articles WHERE title = :title")
    override fun getArticleByTitle(title : String) : CachedArticle

    @Query("DELETE FROM articles WHERE title = :title")
    override fun removeArticleByTitle(title: String)

    @Transaction
    override fun saveArticle(article: CachedArticle) {
        insert(article)
    }

    @Update
    fun update(cachedArticle: CachedArticle)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cachedArticle: CachedArticle) : Long
}