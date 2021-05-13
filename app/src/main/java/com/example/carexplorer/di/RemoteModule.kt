package com.example.carexplorer.di


import com.example.carexplorer.data.model.retrofit.usecase.sources.SourcesRepository
import com.example.carexplorer.data.model.retrofit.usecase.sources.SourcesRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
abstract class RemoteModule {

    @Singleton
    @Binds
    abstract fun bindSourcesRepository(repositoryImpl: SourcesRepositoryImpl): SourcesRepository

}