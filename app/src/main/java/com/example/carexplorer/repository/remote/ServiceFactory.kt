package com.example.carexplorer.repository.remote

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceFactory {


    fun makeService() : ApiService {
        val okkHttpClient =
            makeOkHttpClient(
                makeLoggingInterceptor()
            )
        return makeService(
            okHttpClient = okkHttpClient,
            gson = Gson()
        )
    }

    private fun makeService(okHttpClient: OkHttpClient,gson: Gson) : ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-project-id-326ba.firebaseio.com/.json/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(120,TimeUnit.SECONDS)
            .readTimeout(120,TimeUnit.SECONDS)
            .build()
    }


    private fun makeLoggingInterceptor() : HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        return logging
    }
}