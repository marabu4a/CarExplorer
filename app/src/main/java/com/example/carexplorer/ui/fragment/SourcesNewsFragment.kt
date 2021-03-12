package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexplorer.R
import com.example.carexplorer.data.model.Source
import com.example.carexplorer.helpers.SourcesViewModel
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.SourcesNewsPresenter
import com.example.carexplorer.presenter.SourcesNewsPresenterFactory
import com.example.carexplorer.ui.adapter.SourcesAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseListFragment
import com.example.carexplorer.view.SourcesView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sources.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject


class SourcesNewsFragment : BaseListFragment(), SourcesView {
    override val layoutRes: Int = R.layout.fragment_sources
    private var sViewModel = SourcesViewModel()

    override val viewAdapter: BaseAdapter<*> = SourcesAdapter()

    @Inject
    lateinit var presenterFactory: SourcesNewsPresenterFactory

    private val presenter: SourcesNewsPresenter by moxyPresenter {
        presenterFactory.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.stopWork()
    }

    companion object {
        val tag = "sourcesFragment"
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

        lManager = GridLayoutManager(requireActivity(), 2)
        (lManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position % 3 == 0) {
                        true -> 2
                        false -> 1
                    }
                }

            }

        rView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = lManager
            adapter = viewAdapter
            setHasFixedSize(true)
        }


        if (savedInstanceState == null) {
            sViewModel = ViewModelProviders.of(requireActivity()).get(SourcesViewModel::class.java)
            presenter.fetchSources()
        } else {
            endLoading()
        }

        initClickListener()
    }


    private fun initClickListener() {
        viewAdapter.setOnClick(click = { it, v ->
            (it as Source).let {
                parentRouter.navigateTo(Screens.News(it))
            }
        }, longClick = { it, v -> })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_favorites, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorites -> {
                parentRouter.navigateTo(Screens.Favorites())
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun startLoading() {
        recyclerView.visibility = View.GONE
        cpvSources.visibility = View.VISIBLE
    }

    override fun showSources(sources: List<Source>) {
        sViewModel.changeDataSources(sources)
        viewAdapter.clear()
        viewAdapter.add(sources)
        viewAdapter.notifyDataSetChanged()
    }

    override fun endLoading(
    ) {
        cpvSources.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun showMessage(textResource: Int) {
        Snackbar.make(
            clSources,
            textResource,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }

}