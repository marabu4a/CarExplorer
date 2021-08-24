package com.example.carexplorer.ui.toolbar

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isVisible
import com.example.carexplorer.R
import com.example.carexplorer.helpers.util.inflate
import kotlinx.android.synthetic.main.component_base_toolbar.view.*
import kotlinx.android.synthetic.main.component_default_toolbar_title.view.*

open class DefaultToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseToolbar(context, attrs, defStyleAttr) {

    var title: String? = null
        get() = defaultToolbarTitle.run {
            text.toString().takeIf { isVisible && it.isNotEmpty() }
        }
        set(value) {
            field = value
            with(defaultToolbarTitle) {
                text = value
                isVisible = value != null
            }
            if (value != null) toolbar.title = null
        }

    var xlTitle: String? = null
        get() = toolbar.title.toString()
        set(value) {
            field = value
            toolbar.title = value
            defaultToolbarTitle.isVisible = value == null
        }

    init {
        with(toolbar) {
            inflate(R.layout.component_default_toolbar_title, true)
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.DefaultToolbar,
                0, 0
            ).apply {
                getString(R.styleable.DefaultToolbar_title)?.let {
                    defaultToolbarTitle.text = it
                }
                getString(R.styleable.DefaultToolbar_xl_title)?.let {
                    xlTitle = it
                }
                recycle()
            }
        }
    }
}