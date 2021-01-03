package com.example.carexplorer.di

import com.example.carexplorer.helpers.navigation.LocalCiceroneHolder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalNavigationHolder {

    @Provides
    @Singleton
    fun provideLocalNavigationHolder() : LocalCiceroneHolder = LocalCiceroneHolder()
}