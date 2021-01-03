package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.carexplorer.R
import com.example.carexplorer.data.model.Category
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.presenter.CategoriesPresenter
import com.example.carexplorer.presenter.CategoriesPresenterFactory
import com.example.carexplorer.ui.adapter.CategoriesAdapter
import com.example.carexplorer.ui.adapter.FragmentLifecycle
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseListFragment
import com.example.carexplorer.view.CategoriesView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_categories.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject


class CategoriesFragment : BaseListFragment(),CategoriesView,FragmentLifecycle {

    override val layoutRes: Int = R.layout.fragment_categories
    override val viewAdapter: BaseAdapter<*> = CategoriesAdapter()


    companion object {
        val tag = "categoriesFragment"
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stopWork()
    }

    @Inject
    lateinit var presenterFactory: CategoriesPresenterFactory

    private val presenter: CategoriesPresenter by moxyPresenter {
        presenterFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        rView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = lManager
            adapter = viewAdapter
            setHasFixedSize(true)
        }

        presenter.loadCategories()

        setOnItemClickListener { it,v ->
            (it as Category).let {
                router.navigateTo(Screens.ListArticles(it))
            }
        }
    }

    override fun startLoading() {
        recyclerView.visibility = View.GONE
        cpvCategories.visibility = View.VISIBLE
    }

    override fun endLoading() {
        recyclerView.visibility = View.VISIBLE
        cpvCategories.visibility = View.GONE
    }

    override fun showMessage(textResource: Int) {
        Snackbar.make(
            rlCategories,
            textResource,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }

    override fun getListCategories(listCategories : List<Category>) {
        viewAdapter.clear()
        viewAdapter.add(listCategories)
        viewAdapter.notifyDataSetChanged()
    }

    override fun onPauseFragment() {
        Log.e("Activity","isPausedddd")
    }

    override fun onResumeFragment() {


    }

}