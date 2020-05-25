package com.example.carexplorer.repository.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.carexplorer.data.model.CachedArticle

@Database(entities = [CachedArticle::class],version = 3,exportSchema = false)
 abstract class ContentDatabase : RoomDatabase() {

     abstract val contentDao: ContentDao

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