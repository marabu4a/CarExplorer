package com.example.carexplorer.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.carexplorer.ui.fragment.CategoriesFragment
import com.example.carexplorer.ui.fragment.NewsFeedFragment

class HomeViewPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> NewsFeedFragment()
            else -> CategoriesFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Лента"
            else -> "Материалы"
        }

    }
}