package com.example.carexplorer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.presenter.FavoritesPresenter
import com.example.carexplorer.presenter.FavoritesPresenterFactory
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.view.FavoritesView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.nothing_items.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class FavoritesFragment : BaseFragment(), FavoritesView {
    override val layoutRes: Int = R.layout.fragment_favorites
    var filterItems = arrayListOf<CachedArticle>()
    private var favoritesAdapter: BaseAdapter<*>? = null

    @Inject
    lateinit var presenterFactory: FavoritesPresenterFactory

    private val presenter: FavoritesPresenter by moxyPresenter {
        presenterFactory.create()
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //favoritesAdapter = FavoritesAdapter(
        //
        //)
        favoritesRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)

        }
        //presenter.fetchCachedArticles()
        initClickListener()

    }

    override fun showContent(list: List<CachedArticle>) {
        //if (list.isEmpty()) {
        //    layout_nothing_items.visibility = View.VISIBLE
        //} else {
        //    layout_nothing_items.visibility = View.GONE
        //}
        //if (filterItems.isEmpty()) {
        //    filterItems.addAll(list)
        //}
        //viewAdapter.clear()
        //viewAdapter.add(list)
        //viewAdapter.notifyDataSetChanged()
    }
    //override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //    when (item.itemId) {
    //        R.id.filter -> {
    //            bottomSheetFilter!!.show(childFragmentManager,"BottomSheetFilter")
    //        }
    //    }
    //    return super.onOptionsItemSelected(item)
    //}

    //override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    //    inflater.inflate(R.menu.menu_filter,menu)
    //    super.onCreateOptionsMenu(menu, inflater)
    //}

    override fun showMessage(textResource: Int) {
        Snackbar.make(
            rlFavorites,
            textResource,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }

    override fun showLoading() {
        layout_nothing_items.visibility = View.GONE
        favoritesRV.visibility = View.GONE
    }

    override fun hideLoading() {
        favoritesRV.visibility = View.VISIBLE
    }

    @SuppressLint("RestrictedApi")
    private fun initClickListener() {
        //viewAdapter.setOnClick(
        //    click = {
        //
        //            item, v ->
        //        (item as CachedArticle).let {
        //            when (item.type) {
        //                "news" -> {
        //                    parentRouter.navigateTo(Screens.WebPageScreen(it.title, it.url!!))
        //                }
        //                else -> {
        //                    parentRouter.navigateTo(Screens.ArticleScreen(it))
        //                }
        //            }
        //        }
        //    },
        //    longClick = { item, v ->
        //        (item as CachedArticle).let { article ->
        //            val wrapper = ContextThemeWrapper(requireActivity(), R.style.popupMenuBack)
        //            val popupMenu = androidx.appcompat.widget.PopupMenu(wrapper, v)
        //            popupMenu.inflate(R.menu.meni_clicked_favorite)
        //            popupMenu.setOnMenuItemClickListener { menuItem ->
        //                when (menuItem.itemId) {
        //                    R.id.deleteItem -> {
        //                        presenter.deleteCachedArticles(article)
        //                        filterItems.clear()
        //                        filterItems.addAll(viewAdapter.removeArticle(article.title))
        //                        true
        //                    }
        //                    else -> {
        //                        true
        //                    }
        //                }
        //            }
        //            val menu = MenuPopupHelper(requireActivity(), popupMenu.menu as MenuBuilder, v)
        //            menu.setForceShowIcon(true)
        //            menu.show()
        //        }
        //    })
    }

}

