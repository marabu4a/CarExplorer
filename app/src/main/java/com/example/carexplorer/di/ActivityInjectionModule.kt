package com.example.carexplorer.di

import com.example.carexplorer.di.scope.ActivityScope
import com.example.carexplorer.ui.base.BaseActivity
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector


@Module(includes = [AndroidInjectionModule::class])
interface ActivityInjectionModule {

    @ContributesAndroidInjector(
        modules = [FragmentInjectionModule::class]
    )
    @ActivityScope
    fun activityInjector() : BaseActivity
}