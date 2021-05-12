package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexplorer.R
import com.example.carexplorer.data.model.enities.Source
import com.example.carexplorer.di.injectViewModel
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.SourcesPresenter
import com.example.carexplorer.presenter.SourcesPresenterFactory
import com.example.carexplorer.ui.adapter.SourcesAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseListFragment
import com.example.carexplorer.ui.base.mvi.MviBaseFragment
import com.example.carexplorer.view.SourcesView
import com.example.carexplorer.viewmodel.SourcesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sources.*
import kotlinx.coroutines.flow.collectLatest
import moxy.ktx.moxyPresenter
import timber.log.Timber
import javax.inject.Inject


class SourcesFragment : BaseListFragment(), SourcesView,
    MviBaseFragment<SourcesViewModel.SourcesViewModelState> {
    override val layoutRes: Int = R.layout.fragment_sources

    override val viewAdapter: BaseAdapter<*> = SourcesAdapter()

    @Inject
    lateinit var presenterFactory: SourcesPresenterFactory

    private val presenter: SourcesPresenter by moxyPresenter {
        presenterFactory.create()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sourcesViewModel: SourcesViewModel

    companion object {
        val tag = "sourcesFragment"
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


        lifecycleScope.launchWhenCreated {
            sourcesViewModel.status.collectLatest(::render)
        }

        initClickListener()
    }


    private fun initClickListener() {
        viewAdapter.setOnClick(click = { it, v ->
            (it as Source).let {
                parentRouter.navigateTo(Screens.SourceNews(it))
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


    override fun showLoading() {
        recyclerView.visibility = View.GONE
        cpvSources.visibility = View.VISIBLE
    }

    override suspend fun render(state: SourcesViewModel.SourcesViewModelState) {
        when (state) {
            SourcesViewModel.SourcesViewModelState.Loading -> {
                presenter.startLoading()
            }
            is SourcesViewModel.SourcesViewModelState.Error -> {
                Timber.e(state.error)
                presenter.showError(state.error)
            }
            is SourcesViewModel.SourcesViewModelState.Success -> {
                presenter.stopLoading()
                viewAdapter.clear()
                viewAdapter.add(state.data)
                viewAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun hideLoading() {
        cpvSources.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun showMessage(text: String) {
        Snackbar.make(
            clSources,
            text,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }

}