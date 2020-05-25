package com.example.carexplorer.repository.remote

import com.example.carexplorer.data.model.Source
import javax.inject.Inject

class SourcesRepositoryImpl @Inject constructor(
    private val service: ApiService
) : SourcesRepository {
    override suspend fun getSources(url: String): List<Source> {
        return service.getSources(url)
    }
}