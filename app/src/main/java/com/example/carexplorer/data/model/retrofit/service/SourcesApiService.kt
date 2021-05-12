package com.example.carexplorer.data.model.retrofit.service

import com.example.carexplorer.data.model.retrofit.response.SourcesResponse
import retrofit2.Response
import retrofit2.http.GET

interface SourcesApiService {

    @GET("")
    suspend fun getSources(): Response<SourcesResponse>
}