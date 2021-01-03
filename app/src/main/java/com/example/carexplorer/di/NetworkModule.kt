package com.example.carexplorer.di

import com.example.carexplorer.repository.remote.ApiService
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideApiService(gson: Gson,
                          client: OkHttpClient
    ) : ApiService = buildApi(gson,client, CATEGORIES_BASE_URL)

    @Provides
    @Singleton
    fun provideOkHttpClient(httlpLoggingInterceptor: StethoInterceptor) : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httlpLoggingInterceptor)
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideStethoInterceptor(): StethoInterceptor =
        StethoInterceptor()

    @Provides
    @Singleton
    fun provideGsonConverter() : Gson = Gson()

    companion object {
        const val CATEGORIES_BASE_URL = "https://my-project-id-326ba.firebaseio.com/.json/"
        const val SOURCES_BASE_URL = "https://my-project-id-326ba.firebaseio.com/.json/"
    }

}

inline fun <reified T> buildApi(
    gson: Gson,
    client: OkHttpClient,
    host: String
): T = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(host)
    .client(client)
    .build()
    .create(T::class.java)