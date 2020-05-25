package com.example.carexplorer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.di.App
import com.example.carexplorer.helpers.BottomSheetFilter
import com.example.carexplorer.presenter.FavoritesPresenter
import com.example.carexplorer.ui.adapter.FavoritesAdapter
import com.example.carexplorer.ui.base.BaseListFragment
import com.example.carexplorer.view.FavoritesView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.nothing_items.*
import javax.inject.Inject

class FavoritesFragment : BaseListFragment(),FavoritesView,BottomSheetFilter.CallbackFilter {
    override val layoutId: Int = R.layout.fragment_favorites
    override var titleToolbar = "Избранное"
    var filterItems = arrayListOf<CachedArticle>()
    private lateinit var popMenuAnchor : View
    private var bottomSheetFilter : SuperBottomSheetFragment? = BottomSheetFilter(this)

    override val viewAdapter = FavoritesAdapter()

    @Inject
    @InjectPresenter
    lateinit var presenter: FavoritesPresenter

    @ProvidePresenter
    fun provide() = presenter


    init {
        App.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        base {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)

        }
    }


    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        presenter.fetchCachedArticles()


        initClickListener()


    }

    override fun startLoading() {
        layout_nothing_items.visibility = View.GONE
        recyclerView.visibility = View.GONE
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

    override fun endLoading() {
        recyclerView.visibility = View.VISIBLE
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
        presenter.filterList(tab,filterItems)
    }

    override fun showMessage(textResource:Int) {
        Snackbar.make(
            rlFavorites,
            textResource,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }



    @SuppressLint("RestrictedApi")
    private fun initClickListener() {
        viewAdapter.setOnClick(
            click = {

                    item,v -> (item as CachedArticle).let {
                when (item.type) {
                    "news" -> {
                        navigator.showWebPage(requireActivity(),it.url,it.title)
                    }
                    else -> {
                        navigator.showArticle(requireActivity(),it)
                    }
                }
                }
            },
            longClick = { item, v ->
                (item as CachedArticle).let { article ->
                    val wrapper = ContextThemeWrapper(requireActivity(),R.style.popupMenuBack)
                    val popupMenu = androidx.appcompat.widget.PopupMenu(wrapper,v)
                    popupMenu.inflate(R.menu.meni_clicked_favorite)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.deleteItem -> {
                                presenter.deleteCachedArticles(article)
                                filterItems.clear()
                                filterItems.addAll(viewAdapter.removeArticle(article.title))


                                        Log.e("Activity",filterItems.size.toString())



                                true
                            }
                            else -> {
                                true
                            }
                        }
                    }
                    val menu = MenuPopupHelper(requireActivity(),popupMenu.menu as MenuBuilder,v)
                    menu.setForceShowIcon(true)
                    menu.show()
                }
            })
    }


}

