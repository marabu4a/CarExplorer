package com.example.carexplorer.ui.base

import android.os.Bundle
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import javax.inject.Inject

abstract class BaseFragment : MvpAppCompatFragment() {
    abstract val layoutId : Int
    abstract var titleToolbar : String

    open val showToolbar = true

    @Inject
    lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutId,container,false)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()

        base {
            if (showToolbar) supportActionBar?.show() else supportActionBar?.hide()
            supportActionBar?.title = titleToolbar


        }
    }
    open fun onBackPressed() {}

    fun hideSoftKeyboard() = base { hideSoftKeyboard() }

    inline fun base(block: BaseActivity.() -> Unit) {
        activity.base(block)
    }

    fun close() = fragmentManager?.popBackStack()

}
