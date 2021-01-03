package com.example.carexplorer.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.example.carexplorer.util.hideKeyboard
import moxy.MvpAppCompatFragment
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment : MvpAppCompatFragment() {
    @get:LayoutRes
    protected abstract val layoutRes : Int

    @Inject
    protected lateinit var router : Router

    open val isBottomBarVisible: Boolean
        get() = true
    open val isFullScreen: Boolean
        get() = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutRes,container,false).also {
            Timber.v("onCreateView ${javaClass.simpleName}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view?.hideKeyboard()
    }
    open fun onBackPressed() {
        view?.hideKeyboard()
        //TODO разобраться в выходом
//        if (parentFragmentManager.backStackEntryCount > 1) {
//            router.exit()
//        } else {
//            route
//        }
    }

    fun hideSoftKeyboard() = base { hideSoftKeyboard() }

    inline fun base(block: BaseActivity.() -> Unit) {
        activity.base(block)
    }

    fun close() = fragmentManager?.popBackStack()

}
