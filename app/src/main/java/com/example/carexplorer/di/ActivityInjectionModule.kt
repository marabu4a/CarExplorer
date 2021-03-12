package com.example.carexplorer.di

import com.example.carexplorer.ui.activity.AppActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityInjectionModule {

    @ContributesAndroidInjector(
        modules = [FragmentInjectionModule::class]
    )
    fun activityInjector(): AppActivity
}