package com.example.carexplorer.ui.activity

import androidx.appcompat.app.AppCompatActivity

class ActivityProviderImpl : ActivityProvider {
    override var acitvity: AppCompatActivity? = null
}

interface ActivityProvider {
    var acitvity: AppCompatActivity?
}
