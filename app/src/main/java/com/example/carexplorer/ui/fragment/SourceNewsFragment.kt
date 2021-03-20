package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.data.model.Source
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.SourceNewsPresenter
import com.example.carexplorer.presenter.SourceNewsPresenterFactory
import com.example.carexplorer.ui.adapter.NewsAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseListFragment
import com.example.carexplorer.util.ParcelableArgsBundler
import com.example.carexplorer.view.SourceNewsView
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.haveno_items.*
import kotlinx.android.synthetic.main.item_news.view.*
import kotlinx.android.synthetic.main.nothing_search.*
import moxy.ktx.moxyPresenter
import java.util.*
import javax.inject.Inject


@FragmentWithArgs
class SourceNewsFragment : BaseListFragment(), SourceNewsView {
    override val viewAdapter: BaseAdapter<*> = NewsAdapter()

    override val layoutRes: Int = R.layout.fragment_news
    private val listNews: MutableList<CachedArticle> = mutableListOf()
    private val displayList: MutableList<CachedArticle> = mutableListOf()

    @Arg(bundler = ParcelableArgsBundler::class)
    lateinit var source: Source

    @Inject
    lateinit var presenterFactory: SourceNewsPresenterFactory

    private val presenter: SourceNewsPresenter by moxyPresenter {
        presenterFactory.create()
    }

    companion object {
        val tag = "newsFragment"
    }

    override fun showErrorScreen(state : Boolean) {
        if (state) {
            layout_haveno_items.visibility = View.VISIBLE
        }
        else {
            layout_haveno_items.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.fetchNews(source.url, source.title)

        initClickListener()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search,menu)
        val searchItem = menu.findItem(R.id.search)

        if (searchItem != null) {
            val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean { return true }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        displayList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        listNews.forEach {
                            if (it.title.toLowerCase(Locale.getDefault()).contains(search)) {
                                displayList.add(it)
                            }
                        }
                        if (displayList.isEmpty()) {
                            layout_nothing_search.visibility = View.VISIBLE
                        }
                        else layout_nothing_search.visibility = View.GONE
                        viewAdapter.refreshData(displayList)
                    }
                    else {
                        layout_nothing_search.visibility = View.GONE
                        displayList.clear()
                        displayList.addAll(listNews)
                        viewAdapter.refreshData(displayList)
                    }
                    return true
                }
            })

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorites -> {
                parentRouter.navigateTo(Screens.Favorites())
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun showNews(news : List<CachedArticle>) {
        listNews.addAll(news)
        if (news.isNullOrEmpty()) {
            layout_haveno_items.visibility = View.VISIBLE
        }
        else {
            layout_haveno_items.visibility = View.GONE
        }
        viewAdapter.clear()
        viewAdapter.add(news)
        viewAdapter.notifyDataSetChanged()
    }


    override fun showMessage(textResource: Int) {
        val snackBar = Snackbar.make(
            llNews,
            textResource,
            Snackbar.LENGTH_SHORT
        )
        snackBar.setBackgroundTint(resources.getColor(R.color.violet))
        snackBar.show()
    }

    override fun hideLoading() {
        cpvNews.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun showLoading() {
        layout_haveno_items.visibility = View.GONE
        cpvNews.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun initClickListener() {
        viewAdapter.setOnClick(click = { item, v ->
            (item as CachedArticle).let {
                when (v.id) {
                    R.id.button_favorite_news -> {
                        if (v.button_favorite_news.isChecked) {
                            presenter.saveArticleToDb(it)
                        } else {
                            presenter.removeArticleFromDb(it)
                        }
                    }
                    else -> {
                        parentRouter.navigateTo(Screens.WebPage(it.title, it.url!!))
                    }
                }
            }
        }, longClick = { item, v ->
        })
    }

}