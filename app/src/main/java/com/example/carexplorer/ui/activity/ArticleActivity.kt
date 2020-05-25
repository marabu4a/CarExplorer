package com.example.carexplorer.ui.activity

import com.example.carexplorer.ui.base.BaseActivity
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.fragment.ArticleFragment

class ArticleActivity : BaseActivity() {
    override var fragment: BaseFragment = ArticleFragment()

    override val TAG: String = ArticleFragment.tag
}