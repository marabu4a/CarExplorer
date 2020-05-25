package com.example.carexplorer.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.di.App
import com.example.carexplorer.presenter.NewsPresenter
import com.example.carexplorer.repository.remote.ApiService
import com.example.carexplorer.ui.adapter.NewsAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseListFragment
import com.example.carexplorer.view.NewsView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.haveno_items.*
import kotlinx.android.synthetic.main.item_news.view.*
import kotlinx.android.synthetic.main.nothing_search.*
import java.util.*
import javax.inject.Inject

class NewsFragment : BaseListFragment(),NewsView {
    override val viewAdapter: BaseAdapter<*> = NewsAdapter()

    override val layoutId: Int = R.layout.fragment_news
    private val listNews : MutableList<CachedArticle> = mutableListOf()
    private val displayList : MutableList<CachedArticle> = mutableListOf()
    override var titleToolbar = ""
    @Inject
    @InjectPresenter()
    lateinit var presenter : NewsPresenter

    @ProvidePresenter
    fun provide() = presenter


    init {
        App.appComponent.inject(this)
    }

    override fun startLoading() {
        layout_haveno_items.visibility = View.GONE
        cpvNews.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stopWork()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        val tag = "newsFragment"
    }

    override fun onResume() {
        super.onResume()
        base {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        }
    }

    override fun showErrorScreen(state : Boolean) {
        if (state) {
            layout_haveno_items.visibility = View.VISIBLE
        }
        else {
            layout_haveno_items.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        base {
            val args = intent.getBundleExtra("args")
            val url = args.getString(ApiService.PARAM_LINK_NEWS)
            val sourceTitle = args.getString(ApiService.PARAM_TITLE_ARTICLE)
            if (savedInstanceState == null) {
                presenter.loadNews(url!!,sourceTitle!!)
                titleToolbar = sourceTitle!!
            }
            else {
                endLoading()
            }
        }


        initClickListener()
//        setOnItemClickListener { item, v ->
//            (item as CacheArticle).let {
//                when (v.id) {
//                    R.id.button_favorite_news -> {
//                        if (view.button_favorite_news.isChecked) {
//                            Log.e("Activity","true")
//                        }
//                        else {
//                            Log.e("Activity","false")
//                        }
//                    }
//                    else -> {
//                        navigator.showWebPage(requireActivity(),it.url)
//                    }
//                }
//            }
//        }




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
                navigator.showFavorites(requireActivity())
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

    override fun endLoading() {
        cpvNews.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
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



    private fun initClickListener() {
        viewAdapter.setOnClick(click = {
            item,v -> (item as CachedArticle).let {
            when (v.id) {
                R.id.button_favorite_news -> {
                    if (v.button_favorite_news.isChecked) {
                        presenter.saveArticle(it)
                    }
                    else {
                        presenter.removeArticle(it)
                    }
                }
                else -> {
                    navigator.showWebPage(requireActivity(),it.url,it.title)
                }
            }
        }
        },longClick = {item,v ->
        })
    }

}