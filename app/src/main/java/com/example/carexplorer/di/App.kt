package com.example.carexplorer.di

import android.app.Application
import com.example.carexplorer.ui.activity.*
import com.example.carexplorer.ui.base.RouteActivity
import com.example.carexplorer.ui.fragment.*
import dagger.Component
import javax.inject.Singleton

class App : Application() {


    companion object {
        lateinit var appComponent : AppComponent

    }
    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this)).build()
    }
}


@Singleton
@Component(modules = [AppModule::class,RemoteModule::class,CacheModule::class])
interface AppComponent {
    fun inject(routeActivity: RouteActivity) : RouteActivity
    fun inject(categoriesFragment: CategoriesFragment) : CategoriesFragment
    fun inject(listArticlesFragment: ListArticlesFragment) : ListArticlesFragment
    fun inject(articlesFragment: ArticleFragment) : ArticleFragment
    fun inject(homeActivity: HomeActivity) : HomeActivity
    fun inject(categoriesActivity: CategoriesActivity) : CategoriesActivity
    fun inject(listArticlesActivity: ListArticlesActivity) : ListArticlesActivity
    fun inject(sourcesNewsFragment: SourcesNewsFragment): SourcesNewsFragment
    fun inject(newsFragment: NewsFragment) : NewsFragment
    fun inject(newsActivity: NewsActivity) : NewsActivity
    fun inject(webPageActivity: WebPageActivity) : WebPageActivity
    fun inject(webPageFragment: WebPageFragment) : WebPageFragment
    fun inject(newsFeedFragment: NewsFeedFragment) : NewsFeedFragment

    fun inject(randomNewsFeedFragment: RandomNewsFeedFragment): RandomNewsFeedFragment
    fun inject(recentNewsFeedFragment: RecentNewsFeedFragment) : RecentNewsFeedFragment

    fun inject(favoritesActivity: FavoritesActivity) : FavoritesActivity
    fun inject(favoritesFragment: FavoritesFragment) : FavoritesFragment


    fun inject(homeFragment: HomeFragment) : HomeFragment
}