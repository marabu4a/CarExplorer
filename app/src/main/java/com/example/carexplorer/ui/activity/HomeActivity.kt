package com.example.carexplorer.ui.activity
import com.example.carexplorer.ui.base.BaseActivity
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.ui.fragment.HomeFragment

class HomeActivity : BaseActivity() {
    override var fragment: BaseFragment = HomeFragment()

    override val TAG: String = HomeFragment.tag

}