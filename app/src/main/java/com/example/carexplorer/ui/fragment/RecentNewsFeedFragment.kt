package com.example.carexplorer.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.helpers.NewsViewModel
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.RecentNewsFeedPresenter
import com.example.carexplorer.presenter.RecentNewsFeedPresenterFactory
import com.example.carexplorer.ui.adapter.NewsAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseListFragment
import com.example.carexplorer.view.RecentFeedView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_recent_feed.*
import kotlinx.android.synthetic.main.item_news.view.*
import kotlinx.android.synthetic.main.nothing_search.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class RecentNewsFeedFragment : BaseListFragment(), RecentFeedView {
    override val viewAdapter: BaseAdapter<*> = NewsAdapter()
    private val displayList: MutableList<CachedArticle> = mutableListOf()
    private val listRecentArticles = mutableListOf<CachedArticle>()
    override val layoutRes: Int = R.layout.fragment_recent_feed

    companion object {
        val tag = "recentFeedFragment"
    }


    @Inject
    lateinit var presenterFactory: RecentNewsFeedPresenterFactory

    private val presenter: RecentNewsFeedPresenter by moxyPresenter {
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.stopWork()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            ViewModelProviders.of(requireActivity()).get(NewsViewModel::class.java).getNewsList()
                .observe(viewLifecycleOwner, Observer {
                    presenter.handleRecentNewsFeed(it)
                })
        }
        else {
            endLoading()
        }

        initClickListener()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search,menu)
        val searchItem = menu.findItem(R.id.search)

        if (searchItem != null) {
            val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        displayList.clear()
                        val search = newText.toLowerCase()
                        listRecentArticles.forEach {
                            if (it.title!!.toLowerCase().contains(search)) {
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
                        displayList.addAll(listRecentArticles)
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



    override fun showRecentFeed(recentFeed: List<CachedArticle>) {
        listRecentArticles.addAll(recentFeed)
        viewAdapter.clear()
        viewAdapter.add(recentFeed)
        viewAdapter.notifyDataSetChanged()
    }


    override fun endLoading() {
        cpvRecentFeed.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun startLoading() {
        recyclerView.visibility = View.GONE
        cpvRecentFeed.visibility = View.VISIBLE
    }

    override fun showMessage(textResource: Int) {
        Snackbar.make(
            rlRecent,
            textResource,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }


    private fun initClickListener() {
        viewAdapter.setOnClick(click = { item, v ->
            (item as CachedArticle).let {
                when (v.id) {
                    R.id.button_favorite_news -> {
                        if (v.button_favorite_news.isChecked) {
                            presenter.saveArticle(it)
                        } else {
                            presenter.removeArticle(it)
                        }
                    }
                    else -> {
                        parentRouter.navigateTo(Screens.WebPage(it.title, it.url!!))
                    }
                }
            }
        }, longClick = { item, v ->
            (item as CachedArticle).let {

            }
        })
    }
}