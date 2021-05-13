package com.example.carexplorer.data.model.retrofit.usecase.sources

import com.example.carexplorer.data.model.enities.Source
import kotlinx.coroutines.flow.Flow

interface SourcesRepository {
    suspend fun getSources(): Flow<List<Source>>

}