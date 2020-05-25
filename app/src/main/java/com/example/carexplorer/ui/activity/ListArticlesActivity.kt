package com.example.carexplorer.ui.activity

import com.example.carexplorer.ui.base.BaseActivity
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.fragment.ListArticlesFragment

class ListArticlesActivity : BaseActivity() {
    override var fragment: BaseFragment = ListArticlesFragment()

    override val TAG: String = ListArticlesFragment.tag
}