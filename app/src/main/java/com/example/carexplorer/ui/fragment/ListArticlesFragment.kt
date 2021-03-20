package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.*
import com.example.carexplorer.R
import com.example.carexplorer.data.model.CachedArticle
import com.example.carexplorer.data.model.Category
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.ListArticlesPresenter
import com.example.carexplorer.presenter.ListArticlesPresenterFactory
import com.example.carexplorer.ui.adapter.ListArticlesAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseListFragment
import com.example.carexplorer.util.ParcelableArgsBundler
import com.example.carexplorer.view.ListArticlesView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import kotlinx.android.synthetic.main.fragment_categories.recyclerView
import kotlinx.android.synthetic.main.fragment_list_articles.*
import kotlinx.android.synthetic.main.item_preview_article.view.*
import kotlinx.android.synthetic.main.nothing_search.*
import moxy.ktx.moxyPresenter
import java.util.*
import javax.inject.Inject


@FragmentWithArgs
class ListArticlesFragment : BaseListFragment(), ListArticlesView {

    override val viewAdapter: BaseAdapter<*> = ListArticlesAdapter()
    override val layoutRes: Int = R.layout.fragment_list_articles

    private val listEntries: MutableList<CachedArticle> = mutableListOf()
    private val displayList: MutableList<CachedArticle> = mutableListOf()

    @Arg(bundler = ParcelableArgsBundler::class)
    lateinit var category: Category


    @Inject
    lateinit var presenterFactory: ListArticlesPresenterFactory

    private val presenter: ListArticlesPresenter by moxyPresenter {
        presenterFactory.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stopWork()
    }


    companion object {
        val tag = "listArticlesFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getArticles(category.entries)
        initClickListener()

    }

    override fun hideLoading() {
        recyclerView.visibility = View.VISIBLE
        cpvListArticles.visibility = View.GONE
    }

    override fun showLoading() {
        recyclerView.visibility = View.GONE
        cpvListArticles.visibility = View.VISIBLE
    }

    override fun showListArticles(entries: List<CachedArticle>) {
        listEntries.addAll(entries)
        viewAdapter.clear()
        viewAdapter.add(entries)
        viewAdapter.notifyDataSetChanged()
    }

    override fun showMessage(textResource: Int) {
        Snackbar.make(
            llListArticles,
            textResource,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        parentRouter.exit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.search)

        if (searchItem != null) {
            val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        displayList.clear()
                        val search = newText.toLowerCase(Locale.ROOT)
                        listEntries.forEach {
                            if (it.title.toLowerCase(Locale.ROOT).contains(search)) {
                                displayList.add(it)
                            }
                        }
                        if (displayList.isEmpty()) {
                            layout_nothing_search.visibility = View.VISIBLE
                        } else layout_nothing_search.visibility = View.GONE
                        viewAdapter.refreshData(displayList)
                    } else {
                        layout_nothing_search.visibility = View.GONE
                        displayList.clear()
                        displayList.addAll(listEntries)
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

    private fun initClickListener() {
        setOnItemClickListener { item, v ->
            (item as CachedArticle).let {
                when (v.id) {
                    R.id.button_favorite_entry -> {
                        if (v.button_favorite_entry.isChecked) {
                            //presenter.saveEntry(it)
                        } else {
                            //presenter.removeEntry(it)
                        }
                    }
                    else -> {
                        parentRouter.navigateTo(Screens.Article(it))
                    }
                }
            }
        }
    }
}


private fun <T> getList(jsonArray: String?, clazz: Class<T>): List<T> {
    val typeOfT = TypeToken.getParameterized(List::class.java, clazz).type
    return Gson().fromJson(jsonArray, typeOfT)
}