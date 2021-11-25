package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.di.injectViewModel
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.RecentNewsFeedPresenter
import com.example.carexplorer.presenter.RecentNewsFeedPresenterFactory
import com.example.carexplorer.ui.adapter.NewsAdapter
import com.example.carexplorer.ui.adapter.news.NewsLoadStateAdapter
import com.example.carexplorer.ui.adapter.news.NewsPagingAdapter
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.base.BasePagingAdapter
import com.example.carexplorer.ui.fragment.webpage.WebPageBundle
import com.example.carexplorer.view.RecentFeedView
import com.example.carexplorer.viewmodel.SourcesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_recent_feed.*
import kotlinx.coroutines.launch
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class RecentNewsFeedFragment : BaseFragment(), RecentFeedView {

    private var newsAdapter: BasePagingAdapter<News, NewsAdapter.NewsViewHolder>? = null

    override val layoutRes: Int = R.layout.fragment_recent_feed
    private var currentToolbarTitle: String? = "Свежее"

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

    override fun updatePage(data: PagingData<News>) {
        lifecycleScope.launch {
            newsAdapter?.submitData(data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recentFeedToolbar.title = currentToolbarTitle
        newsAdapter = NewsPagingAdapter(onNewsClick = {
            parentRouter.navigateTo(
                Screens.WebPageScreen(
                    WebPageBundle(
                        it.link,
                        it.title,
                        it.image
                    )
                )
            )
        },
            onFavoriteClick = { news ->
                if (news.isFavorite) {
                    presenter.saveArticleToDb(news)
                } else {
                    presenter.removeArticleFromDb(news)
                }
            }).apply {
            withLoadStateHeaderAndFooter(
                header = NewsLoadStateAdapter { refresh() },
                footer = NewsLoadStateAdapter { refresh() }
            )
            addLoadStateListener { loadState ->
                val refreshState = loadState.refresh
                recentNewsRV?.isVisible = refreshState != LoadState.Loading

                (refreshState == LoadState.Loading).also {
                    recentNewsShimmer?.isVisible = it
                    if (it) {
                        recentNewsShimmer?.startShimmer()
                    } else {
                        recentNewsShimmer?.stopShimmer()
                    }
                }

                val errorState = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(context, it.error.message, Toast.LENGTH_LONG).show()
                }

            }
        }
        recentNewsRV.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    override fun showRecentFeed(recentFeed: List<News>) {
        //listRecentArticles.addAll(recentFeed)
        //newsAdapter?.add(recentFeed)
    }

    override fun hideLoading() {
        //cpvRecentFeed.visibility = View.GONE
        recentNewsRV.visibility = View.VISIBLE
    }

    override fun showLoading() {
        recentNewsRV.visibility = View.GONE
        //cpvRecentFeed.visibility = View.VISIBLE
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