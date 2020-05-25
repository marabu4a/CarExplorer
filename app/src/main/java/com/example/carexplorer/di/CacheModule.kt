package com.example.carexplorer.di

import android.content.Context
import com.example.carexplorer.repository.cache.ContentCache
import com.example.carexplorer.repository.cache.ContentDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule {

    @Provides
    @Singleton
    fun provideArticlesCache(contentDatabase: ContentDatabase) : ContentCache {
        return contentDatabase.contentDao
    }

    @Provides
    @Singleton
    fun provideArticlesDatabase(context: Context) : ContentDatabase {
        return ContentDatabase.getInstance(context)
    }
}