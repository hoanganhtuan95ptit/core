package com.simple.navigation

import android.app.Activity
import android.content.ComponentCallbacks
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.*
import com.simple.coreapp.utils.extentions.Event
import com.simple.coreapp.utils.extentions.getViewModelGlobal
import com.simple.coreapp.utils.extentions.postValue
import com.simple.coreapp.utils.extentions.toEvent
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val module = module {

    viewModel { NavigationViewModel() }
}

private val loadKoinModules by lazy {

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
