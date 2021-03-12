package com.example.carexplorer.di


import com.example.carexplorer.App
import com.example.carexplorer.helpers.navigation.LocalCiceroneHolder
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Singleton

@Module
class NavigationModule {

    @Provides
    @Singleton
    fun provideRouter() = App.cicerone.router

    @Provides
    @Singleton
    fun provideNavigatorHolder(): NavigatorHolder {
        return App.cicerone.navigatorHolder
    }

    @Provides
    @Singleton
    fun provideLocalNavigationHolder(): LocalCiceroneHolder {
        return LocalCiceroneHolder()
    }
}