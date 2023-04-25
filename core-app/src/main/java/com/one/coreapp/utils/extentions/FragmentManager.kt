package com.one.coreapp.utils.extentions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2


fun FragmentManager.getAllFragment(): List<Fragment> {

    val list = arrayListOf<Fragment>()

    getAllFragment(list)

    return list
}

fun FragmentManager.getAllFragment(list: ArrayList<Fragment>) {

    var fragments = fragments

    fragments = fragments.groupBy {

        it.view?.parent as? View
    }.mapValues {

        val key = it.key

        if (key is ViewPager) {

            listOf(it.value.getOrNull(key.currentItem))
        } else if (key is ViewPager2) {

            listOf(it.value.getOrNull(key.currentItem))
        } else {

            it.value
        }
    }.flatMap {

        it.value
    }


    fragments.forEach {

        list.add(it)

        it.childFragmentManager.getAllFragment(list)
    }
}