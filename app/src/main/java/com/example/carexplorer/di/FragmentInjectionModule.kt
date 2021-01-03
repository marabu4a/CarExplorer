package com.example.carexplorer.di

import com.example.carexplorer.di.scope.FragmentScope
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.fragment.*
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector

@Module(includes = [AndroidInjectionModule::class])
interface FragmentInjectionModule {

    @ContributesAndroidInjector
    @FragmentScope
    fun fragmentInjector() : BaseFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun articleFragment() : ArticleFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun categoriesFragment() : CategoriesFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun favoritesFragment() : FavoritesFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun homeFragment() : MainFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun listArticlesFragment() : ListArticlesFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun newsFeedFragment() : NewsFeedFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun newsFragment() : NewsFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun randomNewsFeedFragment() : RandomNewsFeedFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun recentNewsFeedFragment() : RecentNewsFeedFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun sourcesNewsFeedFragment() : SourcesNewsFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun webPageFragment() : WebPageFragment

}