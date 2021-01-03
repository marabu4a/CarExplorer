package com.example.carexplorer

import android.app.Application
import com.example.carexplorer.di.*
import com.example.carexplorer.ui.activity.*
import com.example.carexplorer.ui.fragment.*
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class App : Application() , HasAndroidInjector {
    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    companion object {
        lateinit var appComponent : AppComponent

    }
    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        initStetho()

        Timber.plant(Timber.DebugTree())
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build().apply {
                inject(this@App)
            }
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }
}


