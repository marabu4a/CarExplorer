package com.example.carexplorer.ui.toolbar

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.component_base_toolbar.view.*

class SimpleToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DefaultToolbar(context, attrs, defStyleAttr) {

    init {
        with(toolbar) {
            navigationIcon = null

        }
    }

}
