package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.News
import com.example.carexplorer.data.model.enities.Source
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.helpers.util.ParcelableArgsBundler
import com.example.carexplorer.presenter.SourceNewsPresenter
import com.example.carexplorer.presenter.SourceNewsPresenterFactory
import com.example.carexplorer.ui.adapter.NewsAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.view.SourceNewsView
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.haveno_items.*
import moxy.ktx.moxyPresenter
import timber.log.Timber
import javax.inject.Inject

@FragmentWithArgs
class SourceNewsFragment : BaseFragment(), SourceNewsView {
    private var sourceNewsAdapter: BaseAdapter<*>? = null
    override val layoutRes: Int = R.layout.fragment_news
    private val listNews: MutableList<News> = mutableListOf()
    private val displayList: MutableList<News> = mutableListOf()

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
        sourcesNewsToolbar.initSearchMenu {

        }
        sourceNewsAdapter = NewsAdapter(
            onFavoriteClick = {
                if (it.isFavorite) {
                    if (it.isFavorite) {
                        presenter.saveArticleToDb(it)
                    } else {
                        presenter.removeArticleFromDb(it)
                    }
                }
            },
            onNewsClick = {
                parentRouter.navigateTo(Screens.WebPageScreen(it.title,it.link))
            }
        )
        sourceNewsRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = sourceNewsAdapter
        }
        presenter.fetchNews(source.name)
        sourcesNewsToolbar.title = source.name
        sourcesNewsToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    //override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    //    super.onCreateOptionsMenu(menu, inflater)
    //    inflater.inflate(R.menu.menu_search,menu)
    //    val searchItem = menu.findItem(R.id.search)
    //
    //    if (searchItem != null) {
    //        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
    //        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
    //            override fun onQueryTextSubmit(query: String?): Boolean { return true }
    //
    //            override fun onQueryTextChange(newText: String?): Boolean {
    //                if (newText!!.isNotEmpty()) {
    //                    displayList.clear()
    //                    val search = newText.toLowerCase(Locale.getDefault())
    //                    listNews.forEach {
    //                        if (it.title.toLowerCase(Locale.getDefault()).contains(search)) {
    //                            displayList.add(it)
    //                        }
    //                    }
    //                    if (displayList.isEmpty()) {
    //                        layout_nothing_search.visibility = View.VISIBLE
    //                    }
    //                    else layout_nothing_search.visibility = View.GONE
    //                    viewAdapter.refresh(displayList)
    //                }
    //                else {
    //                    layout_nothing_search.visibility = View.GONE
    //                    displayList.clear()
    //                    displayList.addAll(listNews)
    //                    viewAdapter.refresh(displayList)
    //                }
    //                return true
    //            }
    //        })
    //
    //    }
    //}

    override fun onBackPressed() {
        parentRouter.exit()
        super.onBackPressed()
    }


    override fun showNews(news: List<News>) {
        listNews.addAll(news)
        Timber.e(news.size.toString())
        if (news.isNullOrEmpty()) {
            layout_haveno_items.visibility = View.VISIBLE
        } else {
            layout_haveno_items.visibility = View.GONE
        }
        sourceNewsAdapter?.add(news)
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
        sourceNewsRV.visibility = View.VISIBLE
    }

    override fun showLoading() {
        layout_haveno_items.visibility = View.GONE
        cpvNews.visibility = View.VISIBLE
        sourceNewsRV.visibility = View.GONE
    }
}