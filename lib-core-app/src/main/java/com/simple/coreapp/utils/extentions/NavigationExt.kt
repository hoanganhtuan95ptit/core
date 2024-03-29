package com.simple.coreapp.utils.extentions

import android.app.Activity
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.simple.coreapp.TRANSITION

fun FragmentActivity.navigate(directions: NavDirections, navOptions: NavOptions? = null, navigatorExtras: Navigator.Extras? = null) {

    val navController = (supportFragmentManager.fragments.find { it is NavHostFragment } as? NavHostFragment)?.navController ?: return

    navController.navigate(directions, navOptions, navigatorExtras)
}

fun Fragment.navigate(directions: NavDirections, navOptions: NavOptions? = null, navigatorExtras: Navigator.Extras? = null) {

    val navController = (childFragmentManager.fragments.find { it is NavHostFragment } as? NavHostFragment)?.navController ?: return

    navController.navigate(directions, navOptions, navigatorExtras)
}

fun NavController.navigate(directions: NavDirections, navOptions: NavOptions? = null, navigatorExtras: Navigator.Extras? = null) {

    val bundle = directions.arguments

    val navBuilder = NavOptions.Builder()

    (navigatorExtras as? androidx.navigation.fragment.FragmentNavigator.Extras)?.sharedElements?.values?.firstOrNull()?.let {

        bundle.putString(TRANSITION, it)
    }

    try {

        navigate(directions.actionId, bundle, navOptions ?: navBuilder.build(), navigatorExtras)
    } catch (e: Exception) {

    }
}


fun Fragment.navigateUp() {

    val navController = findNavController()

    navController.navigateUp()
}


fun Activity.findThisNavController(): NavController {

    val view = findViewById<ViewGroup>(android.R.id.content).findFragmentContainerView()
        ?: error("")

    return Navigation.findNavController(view)
}

fun Fragment.findThisNavController(): NavController {

    var findFragment: Fragment? = this

    while (findFragment != null) {

        if (findFragment is NavHostFragment) {
            return findFragment.navController
        }

        val primaryNavFragment = findFragment.childFragmentManager.primaryNavigationFragment

        if (primaryNavFragment is NavHostFragment) {
            return primaryNavFragment.navController
        }

        findFragment = findFragment.childFragmentManager.fragments[0]
    }

    val view = view
    if (view != null) {
        return Navigation.findNavController(view)
    }

    throw IllegalStateException("Fragment  does not have a NavController set")
}