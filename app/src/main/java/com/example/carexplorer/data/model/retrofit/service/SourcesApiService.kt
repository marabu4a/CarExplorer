package com.example.carexplorer.data.model.retrofit.service

import com.example.carexplorer.data.model.Source
import retrofit2.Response
import retrofit2.http.GET

interface SourcesApiService {

    @GET(".json/")
    suspend fun getSources(): Response<List<Source>>
}