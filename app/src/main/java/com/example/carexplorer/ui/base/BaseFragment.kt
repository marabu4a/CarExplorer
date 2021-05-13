package com.example.carexplorer.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.example.carexplorer.R
import com.example.carexplorer.ui.activity.AppActivity
import com.example.carexplorer.util.hideKeyboard
import com.hannesdorfmann.fragmentargs.FragmentArgs
import dagger.android.support.AndroidSupportInjection
import moxy.MvpAppCompatFragment
import timber.log.Timber

abstract class BaseFragment : MvpAppCompatFragment() {
    @get:LayoutRes
    protected abstract val layoutRes: Int

    /**@returns `true` when fragment requires light status bar with dark icons, `null` when it will handle status bar color itself*/
    open val statusBarLightBackground: Boolean? get() = false
    open val transparentStatusBar: Boolean get() = true
    open val isBottomBarVisible: Boolean
        get() = true
    open val isFullScreen: Boolean
        get() = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutRes, container, false).also {
            Timber.v("onCreateView ${javaClass.simpleName}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        FragmentArgs.inject(this)
        super.onCreate(savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        view?.hideKeyboard()
    }

    open fun onBackPressed() {
        view?.hideKeyboard()
    }

    fun routeToMainTab() {
        (activity as? AppActivity)?.setSelectedBottomBarItem(
            R.id.bottomBarNews,
            true
        )
    }

}
