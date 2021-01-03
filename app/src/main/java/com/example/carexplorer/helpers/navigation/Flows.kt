package com.example.carexplorer.helpers.navigation

import androidx.fragment.app.Fragment
import com.example.carexplorer.ui.flows.CategoriesFlow
import com.example.carexplorer.ui.flows.FavoritesFlow
import com.example.carexplorer.ui.flows.NewsFlow
import com.example.carexplorer.ui.flows.SourcesFlow
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Flows {

    object Sources : SupportAppScreen() {
        override fun getFragment(): Fragment = SourcesFlow()
    }

    object Favorites : SupportAppScreen() {
        override fun getFragment(): Fragment = FavoritesFlow()
    }

    object Categories : SupportAppScreen() {
        override fun getFragment(): Fragment = CategoriesFlow()
    }

    object News : SupportAppScreen() {
        override fun getFragment(): Fragment = NewsFlow()
    }
}