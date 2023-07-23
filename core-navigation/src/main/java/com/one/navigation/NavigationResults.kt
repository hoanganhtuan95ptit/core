package com.one.navigation

import android.app.Activity
import android.content.ComponentCallbacks
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.*
import com.one.coreapp.BuildConfig
import com.one.coreapp.utils.extentions.Event
import com.one.coreapp.utils.extentions.getViewModelGlobal
import com.one.coreapp.utils.extentions.postValue
import com.one.coreapp.utils.extentions.toEvent
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

private val module = module {

    viewModel { NavigationViewModel() }
}

private val loadKoinModules by lazy {

    if (BuildConfig.DEBUG) Log.d("tuanha1", "loadKoinModules: ")

    loadKoinModules(module)
}


private fun injectNavigationModule() = loadKoinModules


fun ComponentCallbacks.setNavigationResultListener(requestKey: String, listener: FragmentResultListener) {

    injectNavigationModule()

    val owner: LifecycleOwner = (this as? Fragment)?.viewLifecycleOwner ?: (this as? FragmentActivity) ?: return

    findNavigationViewModelOrNull()?.resultEvent?.map { it }?.observe(owner) {

        if (it.peekContent().first == requestKey) {

            val data = it.getContentIfNotHandled() ?: return@observe

            listener.onFragmentResult(data.first, data.second)
        }
    }
}

fun Fragment.setNavigationResult(requestKey: String, result: Bundle) {

    findNavigationViewModelOrNull()?.setFragmentResult(requestKey, result)
}

fun Activity.setNavigationResult(requestKey: String, result: Bundle) {

    findNavigationViewModelOrNull()?.setFragmentResult(requestKey, result)
}

private fun ComponentCallbacks.findNavigationViewModelOrNull(): NavigationViewModel? {

    injectNavigationModule()

    return getViewModelGlobal(NavigationViewModel::class)
}

private open class NavigationViewModel : ViewModel() {

    val result: LiveData<Pair<String, Bundle>> = MediatorLiveData()

    val resultEvent: LiveData<Event<Pair<String, Bundle>>> = result.toEvent()

    fun setFragmentResult(requestKey: String, result: Bundle) {

        this.result.postValue(requestKey to result)
    }
}
