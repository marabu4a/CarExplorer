package com.example.carexplorer.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.carexplorer.ui.fragment.RandomNewsFeedFragment
import com.example.carexplorer.ui.fragment.RecentNewsFeedFragment
import com.example.carexplorer.ui.fragment.SourcesNewsFragment

class ViewPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SourcesNewsFragment()
            1 -> RandomNewsFeedFragment()
            else -> RecentNewsFeedFragment()
        }
    }


    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Подписки"
            1 -> "Случайное"
            else -> "Свежее"
        }

    }
}

interface FragmentLifecycle {
    fun onPauseFragment()
    fun onResumeFragment()
}