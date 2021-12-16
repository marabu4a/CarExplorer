package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Category
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.CategoriesPresenter
import com.example.carexplorer.presenter.CategoriesPresenterFactory
import com.example.carexplorer.ui.adapter.CategoriesAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.view.CategoriesView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_categories.*
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.navigateToScreen
import timber.log.Timber
import javax.inject.Inject

class CategoriesFragment : BaseFragment(), CategoriesView {

    override val layoutRes: Int = R.layout.fragment_categories
    private var categoriesAdapter: BaseAdapter<*>? = null

    companion object {
        val tag = "categoriesFragment"
    }

    @Inject
    lateinit var presenterFactory: CategoriesPresenterFactory

    private val presenter: CategoriesPresenter by moxyPresenter {
        presenterFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesAdapter = CategoriesAdapter(onCategoryClick = {
            Timber.e(it.toString())
            parentRouter.navigateToScreen(Screens.ListArticlesScreen(it))
        })
        categoriesRV.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = categoriesAdapter
        }
    }

    override fun showMessage(textResource: Int) {
        Snackbar.make(
            rlCategories,
            textResource,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }

    override fun getListCategories(listCategories: List<Category>) {
        categoriesAdapter?.apply {
            add(listCategories)
        }
    }

    override fun showLoading() {
        categoriesRV.visibility = View.GONE
        cpvCategories.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        categoriesRV.visibility = View.VISIBLE
        cpvCategories.visibility = View.GONE
    }

}