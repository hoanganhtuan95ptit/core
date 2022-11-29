package com.one.coreapp.ui.base.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

open class PagerAdapter<T> : FragmentStateAdapter {

    private var getTitle: (T) -> String
    private var getFragment: (T) -> Fragment

    private var listItem: List<T> = emptyList()

    constructor(fragment: Fragment, items: List<T>, getTitle: T.() -> String = { "" }, getFragment: T.() -> Fragment) : super(fragment) {
        this.listItem = items
        this.getTitle = getTitle
        this.getFragment = getFragment
    }

    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle, items: List<T>, getTitle: T.() -> String = { "" }, getFragment: T.() -> Fragment) : super(fragmentManager, lifecycle) {
        this.listItem = items
        this.getTitle = getTitle
        this.getFragment = getFragment
    }

    override fun createFragment(position: Int): Fragment {
        return getFragment.invoke(listItem[position])
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    fun getPageTitle(position: Int): CharSequence? {
        return getTitle.invoke(listItem[position])
    }

}