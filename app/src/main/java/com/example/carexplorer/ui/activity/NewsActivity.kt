package com.example.carexplorer.ui.activity

import com.example.carexplorer.ui.base.BaseActivity
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.fragment.NewsFragment

class NewsActivity : BaseActivity() {
    override var fragment: BaseFragment = NewsFragment()

    override val TAG: String = NewsFragment.tag
}