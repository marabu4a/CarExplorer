package com.example.carexplorer

import android.content.Context
import com.example.carexplorer.di.DaggerAppComponent
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import org.adblockplus.libadblockplus.android.AdblockEngine
import org.adblockplus.libadblockplus.android.AndroidHttpClientResourceWrapper
import org.adblockplus.libadblockplus.android.settings.AdblockHelper
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
        initAdblockHelper()
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }

    private fun initAdblockHelper() {
        if (!AdblockHelper.get().isInit) {
            val basePath = getDir(AdblockEngine.BASE_PATH_DIRECTORY, Context.MODE_PRIVATE).absolutePath
            val map: MutableMap<String, Int> = HashMap()
            map[AndroidHttpClientResourceWrapper.EASYLIST] = R.raw.easylist
            map[AndroidHttpClientResourceWrapper.ACCEPTABLE_ADS] = R.raw.exceptionrules
            AdblockHelper
                .get()
                .init(this, basePath, false, AdblockHelper.PREFERENCE_NAME)
                .preloadSubscriptions(AdblockHelper.PRELOAD_PREFERENCE_NAME, map)
        }
    }
}


