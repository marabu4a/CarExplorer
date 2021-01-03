package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.helpers.NewsViewModel
import com.example.carexplorer.helpers.SourcesViewModel
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.presenter.RandomNewsFeedPresenter
import com.example.carexplorer.presenter.RandomNewsFeedPresenterFactory
import com.example.carexplorer.ui.adapter.FragmentLifecycle
import com.example.carexplorer.ui.adapter.NewsAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseListFragment
import com.example.carexplorer.view.PopularFeedView
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import kotlinx.android.synthetic.main.fragment_popular_feed.*
import kotlinx.android.synthetic.main.item_news.view.*
import kotlinx.android.synthetic.main.nothing_search.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject


@FragmentWithArgs
class RandomNewsFeedFragment : BaseListFragment(),PopularFeedView,FragmentLifecycle {
    override val viewAdapter: BaseAdapter<*> = NewsAdapter()
    override val layoutRes: Int = R.layout.fragment_popular_feed
    private lateinit var rViewModel : NewsViewModel
    private val listPopularArticles : MutableList<CachedArticle> = mutableListOf()
    private val displayList : MutableList<CachedArticle> = mutableListOf()


    companion object {
        const val tag = "popularFeedFragment"
    }

    @Inject
    lateinit var presenterFactory : RandomNewsFeedPresenterFactory

    private val presenter : RandomNewsFeedPresenter by moxyPresenter {
        presenterFactory.create()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            rViewModel = ViewModelProviders.of(requireActivity()).get(NewsViewModel::class.java)
            ViewModelProviders.of(requireActivity()).get(SourcesViewModel::class.java)
                .getSourceList().observe(viewLifecycleOwner, Observer {
                presenter.handleFeed(it)
            })
        }
        else {
            endLoading()
        }

        initClickListener()


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
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
                        val search = newText.toLowerCase()
                        listPopularArticles.forEach {
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
                        displayList.addAll(listPopularArticles)
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
                router.navigateTo(Screens.Favorites())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stopWork()
    }



    override fun endLoading() {
        cpvPopularView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }



    override fun startLoading() {
        recyclerView.visibility = View.GONE
        cpvPopularView.visibility = View.VISIBLE
    }

    override fun showPopularFeed(popularFeed: List<CachedArticle>) {
        rViewModel.changeDataNews(popularFeed)
        listPopularArticles.addAll(popularFeed)

        viewAdapter.clear()
        viewAdapter.add(popularFeed)
        viewAdapter.notifyDataSetChanged()
    }


    private fun initClickListener() {
        viewAdapter.setOnClick(click = { item,v -> (item as CachedArticle).let {
            when (v.id) {
                R.id.button_favorite_news -> {
                    if (v.button_favorite_news.isChecked) {
                        presenter.saveArticle(it)
                        Log.e("Activity","Сохранил!")
                    }
                    else {
                        presenter.removeArticle(it)
                        Log.e("Activity","Удалилdd!")
                    }
                }
                else -> {
                    router.navigateTo(Screens.WebPage(it.title,it.url!!))
                }
            }

        }

        },longClick = {
                item,v-> Toast.makeText(requireActivity(),"Работает", Toast.LENGTH_SHORT).show()
        })
    }

    override fun showMessage(textResource: Int) {
        Snackbar.make(
            rlPopular,
            textResource,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }

    override fun onPauseFragment() {
        Log.e("Activity","isPaused")
    }

    override fun onResumeFragment() {
        Log.e("Activity","isResumed")
    }


}