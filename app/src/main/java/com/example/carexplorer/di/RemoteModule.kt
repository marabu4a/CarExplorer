package com.example.carexplorer.di

import com.example.carexplorer.repository.remote.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class RemoteModule {

    @Singleton
    @Binds
    abstract fun bindCategoriesRepostory(repository: CategoriesRepositoryImpl): CategoriesRepository

    @Singleton
    @Binds
    abstract fun bindNewsFeedRepostory(repositoryImpl: NewsFeedRepositoryImpl): NewsFeedRepository

    @Singleton
    @Binds
    abstract fun bindSourcesRepository(repositoryImpl: SourcesRepositoryImpl): SourcesRepository

}