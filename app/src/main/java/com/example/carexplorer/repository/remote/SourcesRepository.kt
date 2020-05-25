package com.example.carexplorer.repository.remote

import com.example.carexplorer.data.model.Source
import retrofit2.http.Url

interface SourcesRepository {
    suspend fun getSources(@Url url: String) : List<Source>
}