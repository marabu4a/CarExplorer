package com.example.carexplorer.ui.flows

import com.example.carexplorer.helpers.navigation.Screens
import com.example.carexplorer.ui.FlowFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class NewsFlow : FlowFragment() {
    override val flowName: String
        get() = TODO("Not yet implemented")

    override fun getLaunchScreen(): SupportAppScreen = Screens.NewsFeed()
}