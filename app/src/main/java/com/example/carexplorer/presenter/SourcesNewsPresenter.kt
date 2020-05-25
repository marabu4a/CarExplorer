package com.example.carexplorer.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.carexplorer.R
import com.example.carexplorer.data.model.Source
import com.example.carexplorer.repository.remote.SourcesRepository
import com.example.carexplorer.view.SourcesView
import kotlinx.coroutines.*
import javax.inject.Inject


@InjectViewState
class SourcesNewsPresenter @Inject constructor(
    repository : SourcesRepository
) : MvpPresenter<SourcesView>() {
    private val presenterJob = Job()
    private var sources : List<Source> = mutableListOf()
    private var sources1 : List<Source> = mutableListOf()
    private var sources2 : List<Source> = mutableListOf()
    private val url = "https://my-first-project-id-9bcf7.firebaseio.com/.json/"
    private val sourcesRepository = repository


    fun fetchSources() {
        try {
            CoroutineScope(Dispatchers.Main + presenterJob).launch {
                viewState.startLoading()
                withContext(Dispatchers.IO) {
                    sources = sourcesRepository.getSources(url)
                }
                viewState.showSources(sources = sources)
                viewState.endLoading()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            viewState.showMessage(R.string.errorLoadingSources)
        }
    }

    fun stopWork() {
        presenterJob.cancel()
    }
}