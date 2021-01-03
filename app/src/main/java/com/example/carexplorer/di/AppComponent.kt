package com.example.carexplorer.di

import android.content.Context
import com.example.carexplorer.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    NetworkModule::class,
    NavigationModule::class,
    CacheModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context) : Builder

        fun build() : AppComponent
    }

    fun inject(app: App)
}