package com.example.carexplorer.di

import com.example.carexplorer.ui.activity.ActivityProvider
import com.example.carexplorer.ui.activity.ActivityProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ActivityProviderModule {

    @Provides
    @Singleton
    fun provideActivityProvider(): ActivityProvider = ActivityProviderImpl()
}
