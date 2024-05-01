package com.simple.navigation

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.simple.coreapp.ui.base.viewmodels.BaseViewModel
import com.simple.coreapp.utils.extentions.Event
import com.simple.coreapp.utils.extentions.getOrAwait
import com.simple.coreapp.utils.extentions.postValue
import com.simple.coreapp.utils.extentions.toEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class NavigationViewModel(private val listProvider: List<NavigationProvider>) : BaseViewModel() {

    val result: LiveData<Pair<String, Bundle>> = MediatorLiveData()

    val resultEvent: LiveData<Event<Pair<String, Bundle>>> = result.toEvent()


    val deepLinkAndListProvider: LiveData<Map<String, List<NavigationProvider>>> = MediatorLiveData()


    init {

        viewModelScope.launch(handler + Dispatchers.IO) {

            listProvider.groupBy {

                it.deepLink()
            }.let {

                deepLinkAndListProvider.postValue(it)
            }
        }
    }


    fun setFragmentResult(requestKey: String, result: Bundle) {

        this.result.postValue(requestKey to result)
    }

    suspend fun getListProvider(deepLink: String): Pair<Map<String, String>, List<NavigationProvider>>? = viewModelScope.async(handler + Dispatchers.IO) {

        val deepLinkUri = Uri.parse(deepLink)

        val deepLinkHost = deepLinkUri.host
        val deepLinkPath = deepLinkUri.path
        val deepLinkScheme = deepLinkUri.scheme

        val deepLinkParams = deepLinkUri.queryParameterNames.associateBy({ it }, { deepLinkUri.getQueryParameter(it) ?: "" })

        val providerList = deepLinkAndListProvider.getOrAwait()?.toList()?.firstOrNull {

            val providerUri = Uri.parse(it.first)

            val providerHost = providerUri.host
            val providerPath = providerUri.path
            val providerScheme = providerUri.scheme

            (providerScheme == null || deepLinkScheme.equals(providerScheme, true))
                    && (providerHost == null || deepLinkHost.equals(providerHost, true))
                    && (providerPath == null || deepLinkPath.equals(providerPath, true))
        }?.second ?: return@async null

        Pair(deepLinkParams, providerList)
    }.await()
}