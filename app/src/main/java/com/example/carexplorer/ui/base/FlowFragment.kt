package com.example.carexplorer.ui.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.example.carexplorer.R
import com.example.carexplorer.helpers.navigation.CustomSupportAppNavigator
import com.example.carexplorer.helpers.navigation.LocalCiceroneHolder
import com.example.carexplorer.helpers.navigation.RouterProvider
import com.example.carexplorer.ui.activity.AppActivity
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import javax.inject.Inject

abstract class FlowFragment : BaseFragment(), RouterProvider {

    override val layoutRes: Int
        get() = R.layout.layout_container

    abstract val flowName: String
    abstract fun getLaunchScreen(): SupportAppScreen

    private val currentFragment
        get() = childFragmentManager.findFragmentById(R.id.layoutContainer) as? BaseFragment

    @Inject
    lateinit var localCiceroneHolder: LocalCiceroneHolder

    private val cicerone: Cicerone<Router> by lazy {
        localCiceroneHolder.getCicerone(flowName)
    }

    override val ciceroneRouter: Router by lazy {
        cicerone.router
    }

    private val navigatorHolder: NavigatorHolder by lazy {
        cicerone.navigatorHolder
    }

    private val navigator: Navigator by lazy {
        CustomSupportAppNavigator(
            requireActivity(),
            childFragmentManager,
            R.id.layoutContainer
        )
    }

    open fun toStartFlow() {
        ciceroneRouter.newRootScreen(getLaunchScreen())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (childFragmentManager.fragments.isEmpty()) {
            ciceroneRouter.newRootScreen(getLaunchScreen())
        }

        childFragmentManager.addOnBackStackChangedListener {
            if (isVisible) currentFragment?.let {
                (activity as? AppActivity)?.setBottomNavbarVisibility(it.isBottomBarVisible)
                it.statusBarLightBackground?.let { isLight ->
                    (activity as? AppActivity)?.setStatusBarLightBackground(isLight)
                }
                if (it.transparentStatusBar) activity?.window?.apply {
                    statusBarColor = Color.TRANSPARENT
                }
            }
        }
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment?.let {
            (activity as? AppActivity)?.setBottomNavbarVisibility(it.isBottomBarVisible)
            it.statusBarLightBackground?.let { isLight ->
                (activity as? AppActivity)?.setStatusBarLightBackground(isLight)
            }
            if (it.transparentStatusBar) activity?.window?.apply {
                statusBarColor = Color.TRANSPARENT
            }
        }
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
