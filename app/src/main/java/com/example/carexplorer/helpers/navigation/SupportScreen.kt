package com.example.carexplorer.helpers.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

abstract class SupportScreen(private val block: () -> Fragment) : SupportAppScreen() {
    override fun getFragment(): Fragment = block()
}
