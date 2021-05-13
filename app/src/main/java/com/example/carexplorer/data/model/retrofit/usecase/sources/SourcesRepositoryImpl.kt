package com.example.carexplorer.data.model.retrofit.usecase.sources

import com.example.carexplorer.data.model.enities.Source
import com.example.carexplorer.data.model.retrofit.service.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SourcesRepositoryImpl @Inject constructor(
    private val api: ApiService
) : SourcesRepository {
    override suspend fun getSources(): Flow<List<Source>> = flow {
        val response = api.getSources()
        if (response.isSuccessful) {
            emit(response.body()?.sources!!)
        }

    }
}