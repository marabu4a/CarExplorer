package com.example.carexplorer.ui.activity

import com.example.carexplorer.ui.base.BaseActivity
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.fragment.CategoriesFragment

class CategoriesActivity : BaseActivity() {
    override var fragment: BaseFragment = CategoriesFragment()

    override val TAG: String = CategoriesFragment.tag

}