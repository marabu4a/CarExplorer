package com.example.carexplorer.di

import android.app.Application
import android.content.Context
import com.example.carexplorer.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityInjectionModule::class,
        ActivityProviderModule::class,
        AndroidInjectionModule::class,
        NetworkModule::class,
        RemoteModule::class,
        NavigationModule::class,
        ParserModule::class,
        UtilModule::class,
        CacheModule::class]
)

interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    override fun inject(app: App)
}