package com.example.carexplorer.data.cache

import androidx.room.*
import com.example.carexplorer.data.model.enities.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao : ArticlesCache {

    @Query("SELECT * FROM articles_table")
    override fun getArticles() : Flow<List<Article>>

    @Query("SELECT * FROM articles_table WHERE title = :title")
    override fun getArticleByTitle(title : String) : Article

    @Query("DELETE FROM articles_table WHERE title = :title")
    override fun removeArticleByTitle(title: String)

    @Transaction
    override fun saveArticle(article: Article) {
        insert(article)
    }

    @Update
    fun update(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article) : Long
}