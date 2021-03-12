package com.example.carexplorer

import com.example.carexplorer.di.DaggerAppComponent
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import timber.log.Timber

class App : DaggerApplication() {
    private val applicationInjector =
        DaggerAppComponent.builder().application(this).context(this).build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = applicationInjector

    companion object {
        lateinit var cicerone: Cicerone<Router>
            private set
    }

    override fun onCreate() {
        super.onCreate()
        cicerone = Cicerone.create()
        initStetho()
        Timber.plant(Timber.DebugTree())
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }
}


