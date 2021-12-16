package com.example.carexplorer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.data.model.enities.Favorite
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.helpers.FavoritesBottomSheet
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.FavoritesPresenter
import com.example.carexplorer.presenter.FavoritesPresenterFactory
import com.example.carexplorer.ui.adapter.FavoritesAdapter
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.fragment.webpage.WebPageBundle
import com.example.carexplorer.view.FavoritesView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.nothing_items.*
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.navigateToScreen
import javax.inject.Inject

class FavoritesFragment : BaseFragment(), FavoritesView {
    override val layoutRes: Int = R.layout.fragment_favorites
    private var favoritesAdapter: FavoritesAdapter? = null

    @Inject
    lateinit var presenterFactory: FavoritesPresenterFactory

    private val presenter: FavoritesPresenter by moxyPresenter {
        presenterFactory.create()
    }

    private var bottomSheetFilter: SuperBottomSheetFragment? = FavoritesBottomSheet(
        firstValuePair = Pair("Все материалы",true ),
        Pair("Новости",false),
        Pair("Статьи",false),
        OnSelected = {
            favoritesToolbar.title = it
        }
    )

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesToolbar.initSearchMenu {

        }
        favoritesToolbar.onClick = {
            bottomSheetFilter?.show(childFragmentManager,"FavoritesBottomSheet")
        }
        favoritesAdapter = FavoritesAdapter(
            {
                onItemClick(it)
            },{
                presenter.deleteCachedEntry(it)
            }
        )
        favoritesRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = favoritesAdapter
        }
        presenter.fetchCachedEntries()
        initClickListener()

    }

    override fun updateContent(list: ArrayList<Favorite>) {
        favoritesAdapter?.addAll(list)
    }

    override fun showContent(list: ArrayList<Favorite>) {
        if (list.isEmpty()) {
            layout_nothing_items.visibility = View.VISIBLE
        } else {
            layout_nothing_items.visibility = View.GONE
        }
        favoritesAdapter?.addAll(items = list)
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

    private fun onItemClick(item: Favorite) {
        when (item) {
            is News -> {
                parentRouter.navigateToScreen(
                    Screens.WebPageScreen(
                        WebPageBundle(
                            item.link,
                            item.title,
                            item.image
                        )
                    )
                )
            }
            is Article -> {
                parentRouter.navigateToScreen(Screens.ArticleScreen(item))
            }
            else -> throw IllegalArgumentException("invalid")
        }
    }

    private fun onRemoveClick(item: Favorite) {
        when (item) {
            is News -> {

            }
            is Article -> {

            }
            else -> throw IllegalArgumentException("invalid")
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initClickListener() {
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

