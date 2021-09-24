package com.example.carexplorer.di

import com.example.carexplorer.di.scope.FragmentScope
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.base.FlowFragment
import com.example.carexplorer.ui.flows.CategoriesFlow
import com.example.carexplorer.ui.flows.FavoritesFlow
import com.example.carexplorer.ui.flows.NewsFlow
import com.example.carexplorer.ui.flows.SourcesFlow
import com.example.carexplorer.ui.fragment.*
import com.example.carexplorer.ui.fragment.webpage.WebPageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
interface FragmentInjectionModule {

    @ContributesAndroidInjector
    fun fragmentInjector(): BaseFragment

    @ContributesAndroidInjector
    fun flowInjector(): FlowFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun articleFragment(): ArticleFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun categoriesFragment(): CategoriesFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun favoritesFragment(): FavoritesFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun listArticlesFragment(): ListArticlesFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun newsFragment(): SourceNewsFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun recentNewsFeedFragment(): RecentNewsFeedFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun sourcesNewsFeedFragment(): SourcesFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun webPageFragment(): WebPageFragment

    @ContributesAndroidInjector
    @FragmentScope
    fun sourcesFlowFragment(): SourcesFlow

    @ContributesAndroidInjector
    @FragmentScope
    fun favoritesFlowFragment(): FavoritesFlow

    @ContributesAndroidInjector
    @FragmentScope
    fun categoriesFlowFragment(): CategoriesFlow

    @ContributesAndroidInjector
    @FragmentScope
    fun newsFlowFragment(): NewsFlow

}