package com.example.carexplorer.di

import android.content.Context
import com.prof.rssparser.Parser
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class ParserModule {

    @Singleton
    @Provides
    fun provideRssParser(appContext: Context, okHttpClient: OkHttpClient) : Parser = Parser.Builder()
        .context(appContext)
        .cacheExpirationMillis(24L * 60L * 60L * 100L)
        .okHttpClient(okHttpClient)
        .build()
}