package com.example.carexplorer.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.carexplorer.data.model.enities.Source
import com.example.carexplorer.data.model.retrofit.usecase.sources.SourcesRepository
import com.example.carexplorer.ui.base.mvi.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourcesViewModel @Inject constructor(
    private val sourcesRepo: SourcesRepository,
    override val errorHandler: com.example.carexplorer.ui.base.ErrorHandler
) : BaseViewModel(errorHandler) {

    private val _status = MutableStateFlow<SourcesViewModelState>(SourcesViewModelState.Loading)
    val status: StateFlow<SourcesViewModelState> = _status

    private var sourcesLoadJob: Job? = null

    fun fetchSources() {
        _status.value = SourcesViewModelState.Loading
        getSourcesFlow()
    }


    private fun getSourcesFlow() {
        sourcesLoadJob?.cancel()
        _status.value = SourcesViewModelState.Loading
        sourcesLoadJob = viewModelScope.launch(Dispatchers.IO) {
            sourcesRepo.getSources()
                .catch { _status.value = SourcesViewModelState.Error(it.message) }
                .collectLatest {
                    _status.value = SourcesViewModelState.Success(it)
                }
        }
    }


    sealed class SourcesViewModelState : ViewState {
        object Loading : SourcesViewModelState()
        data class Error(val error: String?) : SourcesViewModelState()
        data class Success(val data: List<Source>) : SourcesViewModelState()
    }
}