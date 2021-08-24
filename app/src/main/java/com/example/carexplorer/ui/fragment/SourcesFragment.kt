package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.carexplorer.R
import com.example.carexplorer.di.injectViewModel
import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.helpers.navigation.parentRouter
import com.example.carexplorer.presenter.SourcesPresenter
import com.example.carexplorer.presenter.SourcesPresenterFactory
import com.example.carexplorer.ui.adapter.SourcesAdapter
import com.example.carexplorer.ui.base.BaseAdapter
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.base.mvi.MviBaseFragment
import com.example.carexplorer.view.SourcesView
import com.example.carexplorer.viewmodel.SourcesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sources.*
import kotlinx.coroutines.flow.collectLatest
import moxy.ktx.moxyPresenter
import timber.log.Timber
import javax.inject.Inject

class SourcesFragment : BaseFragment(), SourcesView,
    MviBaseFragment<SourcesViewModel.SourcesViewModelState> {
    override val layoutRes: Int = R.layout.fragment_sources

    private var sourcesAdapter: BaseAdapter<*>? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sourcesAdapter = SourcesAdapter(
            onSourceClick = {
                parentRouter.navigateTo(Screens.SourceNewsScreen(it))
            }
        )
        sourcesRV.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2).apply {
                spanSizeLookup =
                    object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return when (position % 3 == 0) {
                                true -> 2
                                false -> 1
                            }
                        }
                    }
            }
            setHasFixedSize(true)
            adapter = sourcesAdapter

            lifecycleScope.launchWhenCreated {
                sourcesViewModel.status.collectLatest(::render)
            }
        }
    }

    override fun showLoading() {
        sourcesRV.visibility = View.GONE
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
                sourcesAdapter?.add(state.data)
            }
        }
    }

    override fun hideLoading() {
        cpvSources.visibility = View.GONE
        sourcesRV.visibility = View.VISIBLE
    }

    override fun showMessage(text: String) {
        Snackbar.make(
            clSources,
            text,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(resources.getColor(R.color.violet)).show()
    }

}