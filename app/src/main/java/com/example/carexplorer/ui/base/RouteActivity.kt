package com.example.carexplorer.ui.base

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.carexplorer.di.App
import javax.inject.Inject

class RouteActivity : MvpAppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        navigator.showHome(this)

    }

}