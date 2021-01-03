package com.example.carexplorer.ui.base

import android.os.Bundle
import moxy.MvpAppCompatActivity
import javax.inject.Inject

class RouteActivity : MvpAppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator.showHome(this)
    }

}