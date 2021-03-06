package com.example.carexplorer.di

import android.content.Context
import com.example.carexplorer.data.model.retrofit.service.ApiService
import com.example.carexplorer.util.NetworkManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

//    @Singleton
//    @Provides
//    fun provideCategoriesService(
//        gson: Gson,
//        client: OkHttpClient
//    ): CategoriesApiService = buildApi(gson, client, CATEGORIES_BASE_URL)

//    @Singleton
//    @Provides
//    fun providesSourcesService(
//        gson: Gson,
//        client: OkHttpClient
//    ): SourcesApiService = buildApi(gson, client, SOURCES_BASE_URL)

    @Singleton
    @Provides
    fun provideApiService(
        gson: Gson,
        client: OkHttpClient
    ): ApiService = buildApi(gson, client, BASE_URL)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        stethoInterceptor: StethoInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(stethoInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor()

    @Provides
    @Singleton
    fun provideStethoInterceptor(): StethoInterceptor =
        StethoInterceptor()

    @Provides
    @Singleton
    fun provideGsonConverter(): Gson = Gson()

    @Provides
    @Singleton
    fun provideNetworkManager(context: Context): NetworkManager =
        NetworkManager(applicationContext = context.applicationContext)

    companion object {
        const val SERVER_URL = "http://u107086.test-handyhost.ru"
        const val BASE_URL = "$SERVER_URL/rest_api/"
//        const val CATEGORIES_BASE_URL = "https://my-project-id-326ba.firebaseio.com/"
//        const val SOURCES_BASE_URL = "https://my-first-project-id-9bcf7.firebaseio.com/"
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