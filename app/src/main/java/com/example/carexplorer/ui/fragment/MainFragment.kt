package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.dueeeke.tablayout.listener.OnTabSelectListener
import com.example.carexplorer.R
import com.example.carexplorer.helpers.InternetConnection
import com.example.carexplorer.helpers.SegmentTabBehavior
import com.example.carexplorer.ui.adapter.FragmentLifecycle
import com.example.carexplorer.ui.adapter.HomeViewPagerAdapter
import com.example.carexplorer.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*


class MainFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.fragment_home

    companion object {
        val tag = "homeFragment"
    }

    private val titles: Array<String>? = arrayOf("Новости", "Статьи")
    private val mFragments = arrayListOf<Fragment>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        base {
            val isNetworkAvailable = InternetConnection.checkInternetConnection(requireActivity())


            if (!isNetworkAvailable) {

                val builder = AlertDialog.Builder(requireActivity())
                builder.setMessage(R.string.alert_connection_error)
                    .setTitle("Внимание!")
                    .setCancelable(false)
                    .setPositiveButton("Повторить") {
                        dialog, id -> finish()
                        //navigator.showHome(requireActivity())
                    }
                val alert = builder.create()
                alert.show()
            }
            else {
                if (savedInstanceState == null) {
                    //mFragments.add(NewsFeedFragment())
                    //mFragments.add(CategoriesFragment())
                    val layoutParams = tabHome.layoutParams as CoordinatorLayout.LayoutParams
                    layoutParams.behavior = SegmentTabBehavior()
                    val pager = HomeViewPagerAdapter(childFragmentManager)
                    switch_layout_home.adapter = pager
                    switch_layout_home.currentItem = 0
                    switch_layout_home.offscreenPageLimit = 2

                    tabHome.setTabData(
                        titles
                        //requireActivity(),

                        //mFragments
                    )
                    tabHome.setOnTabSelectListener(object : OnTabSelectListener{

                        override fun onTabSelect(position: Int) {
                            switch_layout_home.currentItem = position
                        }

                        override fun onTabReselect(position: Int) {
                        }

                    })

                    switch_layout_home.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                        var currentPosition = 0
                        override fun onPageScrollStateChanged(state: Int) {}

                        override fun onPageScrolled(
                            position: Int,
                            positionOffset: Float,
                            positionOffsetPixels: Int
                        ) {}

                        override fun onPageSelected(position: Int) {
                            val fragmentToShow  = pager.getItem(position) as FragmentLifecycle
                            fragmentToShow.onResumeFragment()

                            val fragmentToHide = pager.getItem(currentPosition) as FragmentLifecycle
                            fragmentToHide.onPauseFragment()

                            currentPosition = position
                            base {
                                when (position) {
                                    0 -> supportActionBar?.setTitle("Новости")
                                    1 ->supportActionBar?.setTitle("Статьи")
                                }
                            }
                            tabHome.currentTab = position
                        }

                    })

                } else {
                    mFragments.add(NewsFeedFragment())
                    mFragments.add(CategoriesFragment())
                    val layoutParams = tabHome.layoutParams as CoordinatorLayout.LayoutParams
                    layoutParams.behavior = SegmentTabBehavior()

                    tabHome.setTabData(
                        titles,
                        requireActivity(),
                        R.id.switch_layout_home,
                        mFragments
                    )
                    tabHome.visibility = View.VISIBLE
                }
            }
        }
    }






}