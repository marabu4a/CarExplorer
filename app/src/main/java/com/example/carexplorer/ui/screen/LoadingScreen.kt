package com.example.carexplorer.ui.screen

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.carexplorer.R
import com.example.carexplorer.helpers.util.inflate

class LoadingScreen @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    init {
        inflate(R.layout.loading_screen, true)
    }
}