package com.example.carexplorer.ui.activity

import com.example.carexplorer.R
import com.example.carexplorer.helpers.navigation.CustomSupportAppNavigator
import dagger.Lazy
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import moxy.MvpAppCompatActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

class AppActivity : MvpAppCompatActivity() , HasAndroidInjector {

    @Inject
    lateinit var navigatorHolder: Lazy<NavigatorHolder>

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any>  = dispatchingAndroidInjector

    private val navigator : Navigator by lazy {
        CustomSupportAppNavigator(this, supportFragmentManager, R.layout.activity_main)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.get().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.get().removeNavigator()
    }

}