package com.example.carexplorer.ui.toolbar

import android.content.Context
import android.util.AttributeSet
import com.example.carexplorer.R
import com.example.carexplorer.helpers.util.inflate
import com.example.carexplorer.helpers.util.setOnDebouncedClickListener
import kotlinx.android.synthetic.main.component_base_toolbar.view.*
import kotlinx.android.synthetic.main.component_spinner_toolbar.view.*

class SpinnerToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseToolbar(context, attrs, defStyleAttr) {

    var onClick: () -> Unit = {}

    var title: String? = null
        set(value) {
            field = value
            defaultToolbarSpinner.text = value
        }
    init {
        with(toolbar) {
            inflate( R.layout.component_spinner_toolbar,true)
            navigationIcon = null
            defaultToolbarSpinner.setOnDebouncedClickListener {
                onClick()
            }
        }

        context.theme.obtainStyledAttributes(
            attrs,R.styleable.SpinnerToolbar,0,0).apply {
                getString(R.styleable.SpinnerToolbar_spinnerTitle).also {
                    title = it
                }
            recycle()
        }
    }
}
