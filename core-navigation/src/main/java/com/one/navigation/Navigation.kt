package com.one.navigation

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.Navigator
import com.one.coreapp.BuildConfig
import com.one.coreapp.ui.dialogs.OptionViewItem
import java.io.Serializable

interface Navigation {


    fun offerNavEvent(event: NavigationEvent) {

        if (onNavigationEvent(event)) {

            return
        }

        event.navigationList.add(this.javaClass.simpleName)

        if (BuildConfig.DEBUG && this is Activity) {

            Log.d("tuanha", "offerNavEvent: ${event.navigationList}")
        }

        if (this is Activity) {

            return
        }

        findParentNavigationOrNull()?.offerNavEvent(event)
    }

    fun onNavigationEvent(event: NavigationEvent): Boolean {

        return false
    }

    private fun findParentNavigationOrNull(): Navigation? {

        if (this !is Fragment) {

            return null
        }

        var fragment: Fragment? = parentFragment

        while (fragment != null && fragment !is Navigation) {

            fragment = fragment.parentFragment
        }

        return if (fragment is Navigation) {

            fragment
        } else {

            this.activity as? Navigation
        }
    }
}

fun Fragment.offerNavEvent(event: NavigationEvent) {

    var fragment: Fragment? = this

    while (fragment != null && fragment !is Navigation) {
        fragment = fragment.parentFragment
    }

    val navigation: Navigation? = if (fragment is Navigation) {

        fragment
    } else {

        this.activity as? Navigation
    }

    navigation?.offerNavEvent(event)
}


open class NavigationEvent : Serializable {

    val navigationList: ArrayList<String> = arrayListOf()
}

open class ScreenEvent(val transitionName: String? = null, val navExtras: Navigator.Extras? = null) : NavigationEvent()


open class BottomSheetEvent<T>(open val keyData: String, open val keyRequest: String, open val list: T) : NavigationEvent()


open class OptionEvent(override val keyData: String, override val keyRequest: String, override val list: List<OptionViewItem>) : BottomSheetEvent<List<OptionViewItem>>(keyData, keyRequest, list)

