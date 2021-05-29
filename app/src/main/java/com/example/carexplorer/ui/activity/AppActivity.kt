package com.example.carexplorer.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.carexplorer.R
import com.example.carexplorer.di.injectViewModel
import com.example.carexplorer.helpers.navigation.CustomSupportAppNavigator
import com.example.carexplorer.helpers.navigation.Flows
import com.example.carexplorer.helpers.navigation.RouterProvider
import com.example.carexplorer.helpers.util.ActionDebouncer
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.base.FlowFragment
import com.example.carexplorer.viewmodel.SourcesViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class AppActivity : MvpAppCompatActivity(), RouterProvider, HasAndroidInjector, AppView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sourcesViewModel: SourcesViewModel

    @Inject
    lateinit var mainActivityProvider: ActivityProvider

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var navigatorHolder: Provider<NavigatorHolder>

    @Inject
    override lateinit var ciceroneRouter: Router

    @Inject
    lateinit var appPresenterProvider: AppPresenter

    private val presenter: AppPresenter by moxyPresenter { appPresenterProvider }


    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector


    private val navigator: Navigator by lazy {
        CustomSupportAppNavigator(this, supportFragmentManager, R.layout.activity_main)
    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.get().setNavigator(navigator)
    }

    private val currentTabFragment: BaseFragment?
        get() = supportFragmentManager.fragments.firstOrNull {
            !it.isHidden && it is BaseFragment
            //&& it.tag != notificationsContainer.tag && it.tag != bottomsheetRootContainer.tag
        } as? BaseFragment

    data class NavigationAction(val menuItem: MenuItem, val wasSelected: Boolean)

    private val navigationActionDebouncer = ActionDebouncer<NavigationAction, Unit> {
        onSelectBottomNavItem(it.menuItem.itemId, it.wasSelected)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        setTheme(R.style.AppTheme)
        mainActivityProvider.acitvity = this

        Timber.e("onCreate AppActivity")
        super.onCreate(savedInstanceState)

        sourcesViewModel = injectViewModel(viewModelFactory)

        if (savedInstanceState == null) {
            sourcesViewModel.fetchSources()
        }

        setContentView(R.layout.activity_main)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN /*or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR*/
                )
        bottomBar.selectedItemId = R.id.bottomBarNews
        initBottomBar()


        ViewCompat.setOnApplyWindowInsetsListener(appActivityRootLayout) { _, insets ->
            if (bottomBar.isVisible) {
                insets.replaceSystemWindowInsets(
                    insets.systemWindowInsetLeft,
                    insets.systemWindowInsetTop,
                    insets.systemWindowInsetRight,
                    (insets.systemWindowInsetBottom - bottomBar.height).coerceAtLeast(0)
                )
            } else insets
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause")
        navigatorHolder.get().removeNavigator()
    }

    override fun onBackPressed() {
        currentTabFragment?.onBackPressed() ?: super.onBackPressed()
    }

    override fun showStartScreen() {
        bottomBar.selectedItemId = R.id.bottomBarNews
        openTab(getTabByMenuItemId(R.id.bottomBarNews))
    }

    override fun resetTabs(exceptProfile: Boolean) {
        with(supportFragmentManager) {
            beginTransaction().apply {
                findFragmentByTag(Flows.NewsScreenFlow.screenKey)?.also { remove(it) }
                findFragmentByTag(Flows.CategoriesScreenFlow.screenKey)?.also { remove(it) }
                findFragmentByTag(Flows.SourcesScreenFlow.screenKey)?.also { remove(it) }
                findFragmentByTag(Flows.FavoritesScreenFlow.screenKey)?.also { remove(it) }
            }.apply {
                if (isStateSaved) commitAllowingStateLoss()
                else commitNow()
            }
        }
    }

    override fun updateNavbarVisibility(isVisibleOnCurrentScreen: Boolean) {
        if (bottomBar.isVisible != isVisibleOnCurrentScreen) {
            bottomBar.isVisible = isVisibleOnCurrentScreen
            mainContainer.requestLayout()
        }
    }


    fun setBottomNavbarVisibility(isVisible: Boolean) {
        presenter.setBottomNavbarVisibility(isVisible)
    }

    fun setStatusBarLightBackground(isBackgroundLight: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isBackgroundLight) {
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility =
                    window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        } else {
            if (isBackgroundLight) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
    }

    fun setSelectedBottomBarItem(itemId: Int, withCallNavListener: Boolean) {
        if (!withCallNavListener) bottomBar.setOnNavigationItemSelectedListener(null)
        bottomBar.selectedItemId = itemId
        if (!withCallNavListener) initBottomBar()
    }

    private fun initBottomBar() {
        bottomBar.apply {
            setOnNavigationItemReselectedListener {
                navigationActionDebouncer.notifyAction(
                    NavigationAction(
                        it, wasSelected = true
                    )
                )
            }
            setOnNavigationItemSelectedListener { menuItem ->
                val isNotified = navigationActionDebouncer.notifyAction(
                    NavigationAction(
                        menuItem, wasSelected = false
                    )
                )
                isNotified
            }
            itemIconTintList = null
        }
    }

    private fun onSelectBottomNavItem(menuItemId: Int, wasSelected: Boolean = false) {
        if (!wasSelected) {
            openTab(getTabByMenuItemId(menuItemId))
        } else {
            (currentTabFragment as? FlowFragment)?.toStartFlow()
        }
    }

    private fun getTabByMenuItemId(itemId: Int) = when (itemId) {
        R.id.bottomBarCategories -> Flows.CategoriesScreenFlow
        R.id.bottomBarFavorites -> Flows.FavoritesScreenFlow
        R.id.bottomBarNews -> Flows.NewsScreenFlow
        R.id.bottomBarSources -> Flows.SourcesScreenFlow
        else -> throw IllegalArgumentException("Unknown itemId")
    }

    private fun openTab(tab: SupportAppScreen) {
        val currentFragment = currentTabFragment
        val newFragment = supportFragmentManager.findFragmentByTag(tab.screenKey)
        Timber.i("try opening $newFragment from $currentFragment")
        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return
        Timber.i("opening $newFragment from $currentFragment")
        supportFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(
                R.id.mainContainer,
                createTabFragment(tab),
                tab.screenKey
            )
            currentFragment?.let { detach(it) }
            newFragment?.let { attach(it) }
        }.apply {
            if (supportFragmentManager.isStateSaved) commitAllowingStateLoss()
            else commitNow()
        }
    }

    private fun createTabFragment(tab: SupportAppScreen): Fragment = tab.fragment!!


}