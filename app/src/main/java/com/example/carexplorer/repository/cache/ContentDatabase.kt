package com.example.carexplorer.repository.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.data.model.enities.News

@Database(entities = [News::class,Article::class],version = 1,exportSchema = false)
 abstract class ContentDatabase : RoomDatabase() {

     abstract val articlesDao: ArticlesDao
     abstract val newsDao : NewsDao

     companion object {

         @Volatile
         private var INSTANCE: ContentDatabase? = null

         fun getInstance(context: Context): ContentDatabase {
             var instance = INSTANCE

             if (instance == null) {
                 instance = Room.databaseBuilder(
                     context.applicationContext,
                     ContentDatabase::class.java,
                     "article_database"
                 )
                     .fallbackToDestructiveMigration()
                     .build()
                 INSTANCE = instance
             }
             return instance
         }

     }
 }
// }