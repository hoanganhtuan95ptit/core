package com.simple.coreapp.utils.ext

import android.content.ComponentCallbacks
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.android.ext.android.getKoinScope
import org.koin.androidx.viewmodel.resolveViewModel
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersHolder
import kotlin.reflect.KClass

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