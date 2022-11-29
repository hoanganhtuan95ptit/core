package com.one.navigation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.*
import com.one.coreapp.BuildConfig
import com.one.coreapp.utils.extentions.Event
import com.one.coreapp.utils.extentions.postValue
import com.one.coreapp.utils.extentions.toEvent
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

private val module = module {

    viewModel { NavigationViewModel() }
}

private val loadKoinModules by lazy {

    if (BuildConfig.DEBUG) Log.d("tuanha", "loadKoinModules: ")

    loadKoinModules(module)
}


private fun injectNavigationModule() = loadKoinModules


fun Any.setNavigationResultListener(requestKey: String, listener: FragmentResultListener) {

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


private fun Any.findNavigationViewModelOrNull(): NavigationViewModel? {

    val activity = (this as? Fragment)?.activity ?: (this as? FragmentActivity) ?: return null

    return KoinJavaComponent.getKoin().getViewModel(activity as ViewModelStoreOwner, NavigationViewModel::class)
}

private open class NavigationViewModel : ViewModel() {

    val result: LiveData<Pair<String, Bundle>> = MediatorLiveData()

    val resultEvent: LiveData<Event<Pair<String, Bundle>>> = result.toEvent()

    fun setFragmentResult(requestKey: String, result: Bundle) {

        this.result.postValue(requestKey to result)
    }
}
