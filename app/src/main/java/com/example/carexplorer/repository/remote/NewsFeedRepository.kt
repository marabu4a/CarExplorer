package com.example.carexplorer.repository.remote

import com.example.carexplorer.data.model.Source
import com.prof.rssparser.Article
import retrofit2.http.Url

interface NewsFeedRepository {
    suspend fun getNewsFeed(@Url url: String) : List<Source>


    suspend fun handleNewsFeed(urlXml : String,sourceTitle : String) : List<Article>
}