package com.example.carexplorer.ui.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.carexplorer.R
import com.example.carexplorer.util.color
import com.example.carexplorer.util.inflate
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.component_base_toolbar.view.*

abstract class BaseToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppBarLayout(context, attrs, defStyleAttr) {

    var isNavigationVisible: Boolean = true
        set(value) {
            field = value
            toolbar.navigationIcon = if (value) ContextCompat.getDrawable(
                context,
                R.drawable.ic_arrow_back_white
            ) else null
        }

    init {
        inflate(R.layout.component_base_toolbar, true)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        stateListAnimator = null
        elevation = 0f
        fitsSystemWindows = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setBackgroundColor(color(R.color.colorAccent))
    }

//    fun initCatalogMenu(
//        onSearchPressed: () -> Unit,
//        onFiltersPressed: () -> Unit
//    ) {
//        with(toolbar) {
//            inflateMenu(R.menu.catalog_menu)
//            setOnMenuItemClickListener {
//                when (it.itemId) {
//                    R.id.action_filters -> onFiltersPressed()
//                    R.id.action_search -> onSearchPressed()
//                }
//                true
//            }
//        }
//    }

//    fun initAddMenu(
//        onAddPressed: () -> Unit
//    ) {
//        with(toolbar) {
//            inflateMenu(R.menu.add_action_menu)
//            setOnMenuItemClickListener {
//                if (it.itemId == R.id.action_add) {
//                    onAddPressed()
//                }
//                true
//            }
//        }
//    }

//    fun initSaveMenu(
//        onSavePressed: () -> Unit
//    ) {
//        with(toolbar) {
//            inflateMenu(R.menu.save_action_menu)
//            setOnMenuItemClickListener {
//                if (it.itemId == R.id.action_save) {
//                    onSavePressed()
//                }
//                true
//            }
//        }
//    }

//    fun initNewsMenu(
//        onFavouriteToggled: (isChecked: Boolean) -> Unit,
//        onSharePressed: () -> Unit
//    ) {
//        with(toolbar) {
//            inflateMenu(R.menu.product_detail_menu)
//            setOnMenuItemClickListener { menuItem ->
//                when (menuItem.itemId) {
//                    R.id.action_add_to_fav -> {
//                        menuItem.isChecked = !menuItem.isChecked
//                        onFavouriteToggled(menuItem.isChecked)
//                        true
//                    }
//                    R.id.action_share -> {
//                        onSharePressed()
//                        true
//                    }
//                    else -> false
//                }
//            }
//        }
//    }

//    fun initFilterMenu(
//        onClearPressed: () -> Unit
//    ) {
//        with(toolbar) {
//            inflateMenu(R.menu.filters_menu)
//            setOnMenuItemClickListener {
//                if (it.itemId == R.id.action_clear_filters) {
//                    onClearPressed()
//                }
//                true
//            }
//        }
//    }

//    fun initSearchMenu(
//        onSearchPressed: () -> Unit
//    ) {
//        with(toolbar) {
//            inflateMenu(R.menu.categories_menu)
//            setOnMenuItemClickListener {
//                when (it.itemId) {
//                    R.id.action_search -> onSearchPressed()
//                }
//                true
//            }
//        }
//    }

//    fun initCloseMenu(
//        onClosePressed: () -> Unit
//    ) {
//        with(toolbar) {
//            inflateMenu(R.menu.close_menu)
//            setOnMenuItemClickListener {
//                when (it.itemId) {
//                    R.id.action_close -> onClosePressed()
//                }
//                true
//            }
//        }
//    }

//    fun setIconAlpha(@IdRes iconId: Int, alpha: Float) {
//        toolbar.menu?.findItem(iconId)?.icon?.apply {
//            mutate()
//            setAlpha((alpha * 255).roundToInt())
//        }
//    }

    fun setNavigationOnClickListener(onClick: () -> Unit) {
        toolbar.setNavigationOnClickListener { onClick() }
    }
}