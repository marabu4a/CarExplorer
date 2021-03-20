package com.example.carexplorer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.helpers.BottomSheetFilter
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.FavoritesPresenter
import com.example.carexplorer.presenter.FavoritesPresenterFactory
import com.example.carexplorer.ui.adapter.FavoritesAdapter
import com.example.carexplorer.ui.base.BaseListFragment
import com.example.carexplorer.view.FavoritesView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_categories.recyclerView
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.nothing_items.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class FavoritesFragment : BaseListFragment(),FavoritesView,BottomSheetFilter.CallbackFilter {
    override val layoutRes: Int = R.layout.fragment_favorites
    var filterItems = arrayListOf<CachedArticle>()

    private var bottomSheetFilter : SuperBottomSheetFragment? = BottomSheetFilter(this)

    override val viewAdapter = FavoritesAdapter()

    @Inject
    lateinit var presenterFactory : FavoritesPresenterFactory

    private val presenter : FavoritesPresenter by moxyPresenter {
        presenterFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        presenter.fetchCachedArticles()


        initClickListener()


    }

    override fun showContent(list: List<CachedArticle>) {
        if (list.isEmpty()) {
            layout_nothing_items.visibility = View.VISIBLE
        }
        else {
            layout_nothing_items.visibility = View.GONE
        }
        if (filterItems.isEmpty()) {
            filterItems.addAll(list)
        }
        viewAdapter.clear()
        viewAdapter.add(list)
        viewAdapter.notifyDataSetChanged()
    }

    override fun dismissBottomSheet(animate : Boolean) {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> {
                bottomSheetFilter!!.show(childFragmentManager,"BottomSheetFilter")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun filter(tab: Int) {
        presenter.filterList(tab, filterItems)
    }

    override fun showMessage(textResource: Int) {
        Snackbar.make(
            rlFavorites,
            textResource,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }

    override fun showLoading() {
        layout_nothing_items.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    override fun hideLoading() {
        recyclerView.visibility = View.VISIBLE
    }

    @SuppressLint("RestrictedApi")
    private fun initClickListener() {
        viewAdapter.setOnClick(
            click = {

                    item, v ->
                (item as CachedArticle).let {
                    when (item.type) {
                        "news" -> {
                            parentRouter.navigateTo(Screens.WebPage(it.title, it.url!!))
                        }
                        else -> {
                            parentRouter.navigateTo(Screens.Article(it))
                        }
                    }
                }
            },
            longClick = { item, v ->
                (item as CachedArticle).let { article ->
                    val wrapper = ContextThemeWrapper(requireActivity(), R.style.popupMenuBack)
                    val popupMenu = androidx.appcompat.widget.PopupMenu(wrapper, v)
                    popupMenu.inflate(R.menu.meni_clicked_favorite)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.deleteItem -> {
                                presenter.deleteCachedArticles(article)
                                filterItems.clear()
                                filterItems.addAll(viewAdapter.removeArticle(article.title))
                                true
                            }
                            else -> {
                                true
                            }
                        }
                    }
                    val menu = MenuPopupHelper(requireActivity(), popupMenu.menu as MenuBuilder, v)
                    menu.setForceShowIcon(true)
                    menu.show()
                }
            })
    }


}

