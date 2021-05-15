package com.example.carexplorer.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.di.injectViewModel
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.RecentNewsFeedPresenter
import com.example.carexplorer.presenter.RecentNewsFeedPresenterFactory
import com.example.carexplorer.ui.adapter.NewsAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.view.RecentFeedView
import com.example.carexplorer.viewmodel.SourcesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_recent_feed.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class RecentNewsFeedFragment : BaseFragment(), RecentFeedView {
    private var newsAdapter: BaseAdapter<*>? = null
    private val displayList: MutableList<News> = mutableListOf()
    private val listRecentArticles = mutableListOf<News>()
    override val layoutRes: Int = R.layout.fragment_recent_feed


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sourcesViewModel: SourcesViewModel


    @Inject
    lateinit var presenterFactory: RecentNewsFeedPresenterFactory

    private val presenter: RecentNewsFeedPresenter by moxyPresenter {
        presenterFactory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sourcesViewModel = injectViewModel(viewModelFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapter = NewsAdapter(onNewsClick = {
            parentRouter.navigateTo(Screens.WebPageScreen(it.title, it.link))
        },
        onFavoriteClick = { news ->
            if (news.isFavorite) {
                presenter.saveArticleToDb(news)
            } else {
                presenter.removeArticleFromDb(news)
            }
        })
        recentNewsRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
        presenter.fetchFeed()
        //lifecycleScope.launchWhenCreated {
        //    sourcesViewModel.status.collectLatest { status ->
        //        when (status) {
        //            is SourcesViewModel.SourcesViewModelState.Success -> {
        //
        //            }
        //        }
        //    }
        //}
    }


    //override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    //    super.onCreateOptionsMenu(menu, inflater)
    //    inflater.inflate(R.menu.menu_search,menu)
    //    val searchItem = menu.findItem(R.id.search)
    //
    //    if (searchItem != null) {
    //        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
    //        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
    //            override fun onQueryTextSubmit(query: String?): Boolean {
    //                return true
    //            }
    //
    //            override fun onQueryTextChange(newText: String?): Boolean {
    //                if (newText!!.isNotEmpty()) {
    //                    displayList.clear()
    //                    val search = newText.toLowerCase()
    //                    listRecentArticles.forEach {
    //                        if (it.title!!.toLowerCase().contains(search)) {
    //                            displayList.add(it)
    //                        }
    //                    }
    //                    if (displayList.isEmpty()) {
    //                        layout_nothing_search.visibility = View.VISIBLE
    //                    }
    //                    else layout_nothing_search.visibility = View.GONE
    //                    viewAdapter.refresh(displayList)
    //
    //                }
    //                else {
    //                    layout_nothing_search.visibility = View.GONE
    //                    displayList.clear()
    //                    displayList.addAll(listRecentArticles)
    //                    viewAdapter.refresh(displayList)
    //                }
    //                return true
    //            }
    //
    //
    //        })
    //
    //    }
    //}


    override fun showRecentFeed(recentFeed: List<News>) {
        listRecentArticles.addAll(recentFeed)
        newsAdapter?.add(recentFeed)
    }

    override fun hideLoading() {
        cpvRecentFeed.visibility = View.GONE
        recentNewsRV.visibility = View.VISIBLE
    }

    override fun showLoading() {
        recentNewsRV.visibility = View.GONE
        cpvRecentFeed.visibility = View.VISIBLE
    }

    override fun showMessage(textResource: Int) {
        Snackbar.make(
            rlRecent,
            textResource,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }

    companion object {
        val tag = "recentFeedFragment"
    }
}