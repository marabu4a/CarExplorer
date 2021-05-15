package com.example.carexplorer.di

import android.content.Context
import com.example.carexplorer.repository.cache.ArticlesCache
import com.example.carexplorer.repository.cache.ContentDatabase
import com.example.carexplorer.repository.cache.NewsCache
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule {

    @Provides
    @Singleton
    fun provideArticlesCache(contentDatabase: ContentDatabase) : ArticlesCache {
        return contentDatabase.articlesDao
    }

    @Provides
    @Singleton
    fun providesNewsCache(contentDatabase: ContentDatabase) : NewsCache {
        return contentDatabase.newsDao
    }

    @Provides
    @Singleton
    fun provideArticlesDatabase(context: Context) : ContentDatabase {
        return ContentDatabase.getInstance(context)
    }
}