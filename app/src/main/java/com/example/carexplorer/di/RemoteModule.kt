package com.example.carexplorer.di

import android.content.Context
import com.example.carexplorer.repository.remote.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RemoteModule {
    @Singleton
    @Provides
    fun provideApiService(): ApiService = ServiceFactory.makeService()
    @Provides
    @Singleton
    fun provideCategoryRepository(service: ApiService): CategoriesRepository {
        return CategoriesRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideSourcesRepository(service: ApiService) : SourcesRepository {
        return SourcesRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideNewsFeedRepository(service: ApiService,context : Context) : NewsFeedRepository {
        return NewsFeedRepositoryImpl(service,context)
    }

}