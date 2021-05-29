package com.example.carexplorer.data.cache

import androidx.room.*
import com.example.carexplorer.data.model.enities.News
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao : NewsCache {

    @Query("SELECT * FROM news_table")
    override fun getNews(): Flow<List<News>>

    @Query("SELECT * FROM news_table WHERE title = :title")
    override fun getNewsByTitle(title: String) : News

    @Query("DELETE FROM news_table WHERE title = :title")
    override fun removeNewsByTitle(title: String)

    @Transaction
    override fun saveNews(news: News) {
        insert(news)
    }

    @Update
    fun update(news: News)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: News) : Long
}