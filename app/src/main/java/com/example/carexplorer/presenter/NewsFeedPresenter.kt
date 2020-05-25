package com.example.carexplorer.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.carexplorer.data.model.Source
import com.example.carexplorer.repository.remote.NewsFeedRepository
import com.example.carexplorer.view.NewsFeedView
import com.prof.rssparser.Article
import kotlinx.coroutines.Job
import javax.inject.Inject

@InjectViewState
class NewsFeedPresenter @Inject constructor(
    repository: NewsFeedRepository
): MvpPresenter<NewsFeedView>() {
    private val presenterJob = Job()
    private val url = "https://my-first-project-id-9bcf7.firebaseio.com/.json/"
    private val newsFeedRepository = repository
    private var sources : List<Source> = mutableListOf()
    private val listArticles: MutableList<Article> = mutableListOf()

//    fun handleNewsFeed(sourcesList: List<Source>) {
//        CoroutineScope(Dispatchers.Main + presenterJob).launch {
//            viewState.startLoadingArticles()
//            withContext(Dispatchers.IO) {
//                fetchNewsFeed(sourcesList)
//            }
//            listArticles.shuffle()
//            viewState.showPopularFeed(listArticles)
//            listArticles.shuffle()
//            viewState.showRecentFeed(listArticles)
//            viewState.endLoadingArticles()
//        }
//    }

//    private suspend fun fetchNewsFeed(sourcesList: List<Source>) {
//        sourcesList.forEach { source ->
//            newsFeedRepository.handleNewsFeed(source.url).forEach { article ->
//                article.pubDate = parseDate(article.pubDate)
//                listArticles.add(article)
//            }
//        }
//        Log.e("Activity",listArticles.size.toString())
//    }

//    fun fetchSources() {
//        CoroutineScope(Dispatchers.Main + presenterJob).launch {
//            viewState.startLoadingSources()
//            withContext(Dispatchers.IO) {
//                sources = newsFeedRepository.getNewsFeed(url)
//            }
//            viewState.showSources(sources = sources)
//            viewState.endLoadingSources()
//            handleNewsFeed(sources)
//        }
//    }

//    fun stopWork() {
//        presenterJob.cancel()
//    }
//
//    private fun parseDate(pubDate : String?) : String? {
//        return try {
//            val sourceSdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH)
//            val date = sourceSdf.parse(pubDate)
//            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
//            sdf.format(date)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//            pubDate
//        }
//    }
}