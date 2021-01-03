package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.carexplorer.R
import com.example.carexplorer.presenter.NewsFeedPresenter
import com.example.carexplorer.presenter.NewsFeedPresenterFactory
import com.example.carexplorer.ui.adapter.FragmentLifecycle
import com.example.carexplorer.ui.adapter.ViewPagerAdapter
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.view.NewsFeedView
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import kotlinx.android.synthetic.main.fragment_newsline.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject


@FragmentWithArgs
class NewsFeedFragment : BaseFragment(),NewsFeedView,FragmentLifecycle {

    @Inject
    lateinit var presenterFactory : NewsFeedPresenterFactory

    private val presenter: NewsFeedPresenter by moxyPresenter {
        presenterFactory.create()
    }

    companion object {
        val tag = "newsFeedFragment"
    }

    override val layoutRes: Int = R.layout.fragment_newsline

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        base {


            if (savedInstanceState == null) {
                val fragmentAdapter = ViewPagerAdapter(childFragmentManager)
                viewPager.adapter = fragmentAdapter
                viewPager.currentItem = 1
                viewPager.offscreenPageLimit = 3

                viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    var currentPosition = 0
                    override fun onPageScrollStateChanged(state: Int) {}

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {}

                    override fun onPageSelected(position: Int) {
                        val fragmentToShow  = fragmentAdapter.getItem(position) as FragmentLifecycle
                        fragmentToShow.onResumeFragment()

                        val fragmentToHide = fragmentAdapter.getItem(currentPosition) as FragmentLifecycle
                        fragmentToHide.onPauseFragment()

                        currentPosition = position
                    }

                })
                tabNewsline.setViewPager(viewPager)
            } else {
                tabNewsline.visibility = View.VISIBLE
            }
        }
    }

    override fun startLoading() {
        TODO("Not yet implemented")
    }

    override fun endLoading() {
        TODO("Not yet implemented")
    }

    override fun onPauseFragment() {
        TODO("Not yet implemented")
    }

    override fun onResumeFragment() {
        TODO("Not yet implemented")
    }

}