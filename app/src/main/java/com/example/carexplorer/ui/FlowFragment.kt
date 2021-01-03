package com.example.carexplorer.ui

import android.os.Bundle
import com.example.carexplorer.R
import com.example.carexplorer.helpers.navigation.CustomSupportAppNavigator
import com.example.carexplorer.helpers.navigation.LocalCiceroneHolder
import com.example.carexplorer.ui.base.BaseFragment
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

abstract class FlowFragment : BaseFragment() {

    override val layoutRes: Int
        get() = R.layout.layout_container

    abstract val flowName : String
    abstract fun getLaunchScreen() : SupportAppScreen

    private val currentFragment
        get() = childFragmentManager.findFragmentById(R.id.layoutContainer) as? BaseFragment

    @Inject
    lateinit var localCiceroneHolder : LocalCiceroneHolder

    private val cicerone : Cicerone<Router> by lazy {
        localCiceroneHolder.getCicerone(flowName)
    }

    private val navigatorHolder : NavigatorHolder by lazy {
        cicerone.navigatorHolder
    }

    private val navigator : Navigator by lazy {
        CustomSupportAppNavigator(
            requireActivity(),
            childFragmentManager,
            R.id.layoutContainer
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (childFragmentManager.fragments.isEmpty()) {
            cicerone.router.newRootScreen(getLaunchScreen())
        }
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }


    override fun onResume() {
        navigatorHolder.setNavigator(navigator)
        super.onResume()
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
