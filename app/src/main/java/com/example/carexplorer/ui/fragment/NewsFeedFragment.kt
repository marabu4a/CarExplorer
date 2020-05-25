package com.example.carexplorer.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.carexplorer.R
import com.example.carexplorer.di.App
import com.example.carexplorer.presenter.NewsFeedPresenter
import com.example.carexplorer.ui.adapter.FragmentLifecycle
import com.example.carexplorer.ui.adapter.ViewPagerAdapter
import com.example.carexplorer.ui.base.BaseFragment
import com.example.carexplorer.view.NewsFeedView
import kotlinx.android.synthetic.main.fragment_newsline.*
import javax.inject.Inject

class NewsFeedFragment : BaseFragment(),NewsFeedView,FragmentLifecycle {
    override val layoutId: Int = R.layout.fragment_newsline
    override val showToolbar: Boolean = true

    override var titleToolbar = "Новости"

    @Inject
    @InjectPresenter
    lateinit var presenter : NewsFeedPresenter

    @ProvidePresenter
    fun provide() = presenter


    init {
        App.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        val tag = "newsFeedFragment"
    }

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

    override fun onPauseFragment() {
        Log.e("Activity","isPaused")
    }

    override fun onResumeFragment() {
        Log.e("Activity","isResumed")
//        base {
//            val kk = actionBar
//            kk!!.title = "Новости"
//        }
    }
}