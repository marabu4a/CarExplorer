package com.example.carexplorer.helpers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carexplorer.data.model.Source

class SourcesViewModel : ViewModel() {
    private lateinit var sources : MutableLiveData<List<Source>>

    fun getSourceList() : MutableLiveData<List<Source>> {
        if (!::sources.isInitialized) {
            sources = MutableLiveData()
        }
        return sources
    }

    fun changeDataSources(list : List<Source>) {
        if (!::sources.isInitialized) {
            sources = MutableLiveData()
        }
        sources.value = list
    }
}