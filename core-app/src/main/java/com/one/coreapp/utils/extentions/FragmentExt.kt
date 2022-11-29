package com.one.coreapp.utils.extentions

import androidx.fragment.app.Fragment

inline fun <reified T> Fragment.findParentFirstOrThis(): Fragment {

    return findParentFirstOrNull<T>() ?: this
}

inline fun <reified T> Fragment.findParentFirstOrNull(): Fragment? {

    var fragment: Fragment? = this.parentFragment

    while (fragment != null && fragment !is T) {
        fragment = fragment.parentFragment
    }

    return if (fragment is T) {
        fragment
    } else {
        null
    }
}


inline fun <reified T> Fragment.findParentLastOrThis(): Fragment {

    return findParentLastOrNull<T>() ?: this
}

inline fun <reified T> Fragment.findParentLastOrNull(): Fragment? {

    var parent: Fragment? = this

    var fragment: T? = null

    while (parent != null) {

        parent = parent.parentFragment

        if (parent is T) fragment = parent
    }

    return fragment as Fragment?
}