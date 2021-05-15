package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Article
import com.example.carexplorer.data.model.enities.Category
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.ListArticlesPresenter
import com.example.carexplorer.presenter.ListArticlesPresenterFactory
import com.example.carexplorer.ui.adapter.ListArticlesAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.util.ParcelableArgsBundler
import com.example.carexplorer.view.ListArticlesView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import kotlinx.android.synthetic.main.fragment_list_articles.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

@FragmentWithArgs
class ListArticlesFragment : BaseFragment(), ListArticlesView {

    private var listArticlesAdapter: BaseAdapter<*>? = null
    override val layoutRes: Int = R.layout.fragment_list_articles

    private val listEntries: MutableList<Article> = mutableListOf()
    private val displayList: MutableList<Article> = mutableListOf()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listArticlesAdapter = ListArticlesAdapter(
            onArticleClick = {
                parentRouter.navigateTo(Screens.ArticleScreen(it))
            },
            onFavoriteClick = { article ->
                if (article.isFavorite) {
                    presenter.saveArticle(article)
                } else {
                    presenter.removeArticle(article)
                }
            }
        )
        listArticlesRV.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listArticlesAdapter
        }
        presenter.fetchArticles(category.name)
    }

    override fun hideLoading() {
        listArticlesRV.visibility = View.VISIBLE
        cpvListArticles.visibility = View.GONE
    }

    override fun showLoading() {
        listArticlesRV.visibility = View.GONE
        cpvListArticles.visibility = View.VISIBLE
    }

    override fun showListArticles(entries: List<Article>) {
        listEntries.addAll(entries)
        listArticlesAdapter?.add(entries)
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

    //override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    //    super.onCreateOptionsMenu(menu, inflater)
    //    inflater.inflate(R.menu.menu_search, menu)
    //    val searchItem = menu.findItem(R.id.search)
    //
    //    if (searchItem != null) {
    //        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
    //        searchView.setOnQueryTextListener(object :
    //            androidx.appcompat.widget.SearchView.OnQueryTextListener {
    //            override fun onQueryTextSubmit(query: String?): Boolean {
    //                return true
    //            }
    //
    //            override fun onQueryTextChange(newText: String?): Boolean {
    //                if (newText!!.isNotEmpty()) {
    //                    displayList.clear()
    //                    val search = newText.toLowerCase(Locale.ROOT)
    //                    listEntries.forEach {
    //                        if (it.title.toLowerCase(Locale.ROOT).contains(search)) {
    //                            displayList.add(it)
    //                        }
    //                    }
    //                    if (displayList.isEmpty()) {
    //                        layout_nothing_search.visibility = View.VISIBLE
    //                    } else layout_nothing_search.visibility = View.GONE
    //                    listArticlesAdapter.refresh(displayList)
    //                } else {
    //                    layout_nothing_search.visibility = View.GONE
    //                    displayList.clear()
    //                    displayList.addAll(listEntries)
    //                    listArticlesAdapter.refresh(displayList)
    //                }
    //                return true
    //            }
    //        })
    //
    //    }
    //}
}


private fun <T> getList(jsonArray: String?, clazz: Class<T>): List<T> {
    val typeOfT = TypeToken.getParameterized(List::class.java, clazz).type
    return Gson().fromJson(jsonArray, typeOfT)
}