package com.example.carexplorer.ui.activity

import com.example.carexplorer.ui.base.BaseActivity
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.fragment.WebPageFragment

class WebPageActivity : BaseActivity() {
    override var fragment: BaseFragment = WebPageFragment()

    override val TAG: String = WebPageFragment.tag

}