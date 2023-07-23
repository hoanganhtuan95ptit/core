package com.one.coreapp.utils.extentions

import android.content.ComponentCallbacks
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.one.coreapp.ui.ComponentCache
import org.koin.android.ext.android.getKoinScope
import org.koin.androidx.viewmodel.resolveViewModel
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersHolder
import kotlin.reflect.KClass

inline fun <reified T : ViewModel> Any.getViewModelGlobal(clazz: KClass<T>): T {


    val clazzName = clazz.java.name


    if (this !is ComponentCallbacks) {

        error("Not create ${clazz.simpleName} in : ${this::class.java.simpleName}")
    }


    val mBagOfTags = (this as? ComponentCache)?.mBagOfTags


    (mBagOfTags?.get(clazzName) as? T)?.let {

        return it
    }


    val owner: FragmentActivity = (this as? Fragment)?.activity ?: this as? FragmentActivity ?: error("Not create ${clazz.simpleName} in : ${this::class.java.simpleName}")


    return getViewModel(owner, clazz).apply {

        mBagOfTags?.put(clazzName, this)
    }
}

@MainThread
fun <T : ViewModel> ComponentCallbacks.getViewModel(clazz: KClass<T>, parameters: (() -> ParametersHolder)? = null): T {

    val owner = this as ViewModelStoreOwner

    return getViewModel(owner, clazz, parameters)
}

@OptIn(KoinInternalApi::class)
@MainThread
fun <T : ViewModel> ComponentCallbacks.getViewModel(owner: ViewModelStoreOwner, clazz: KClass<T>, parameters: (() -> ParametersHolder)? = null): T {

    val extras = (owner as? ComponentActivity)?.defaultViewModelCreationExtras ?: (owner as? Fragment)?.defaultViewModelCreationExtras ?: error("")

    return resolveViewModel(
        clazz,
        owner.viewModelStore,
        extras = extras,
        qualifier = null,
        parameters = parameters,
        scope = getKoinScope()
    )
}