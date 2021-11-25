package com.example.carexplorer.ui.adapter.news

import android.content.Context
import android.util.AttributeSet
import com.example.carexplorer.R
import com.example.carexplorer.helpers.util.inflate
import com.facebook.shimmer.ShimmerFrameLayout

class NewsLoadingPlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShimmerFrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(R.layout.item_news_loading, true)
    }
}