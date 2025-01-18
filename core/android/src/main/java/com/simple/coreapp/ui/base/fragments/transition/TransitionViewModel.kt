package com.simple.coreapp.ui.base.fragments.transition

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModels.BaseViewModel
import androidx.lifecycle.asFlow
import com.simple.state.ResultState
import com.simple.state.isSuccess
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

abstract class TransitionViewModel : BaseViewModel() {

    private val transition: MediatorLiveData<HashMap<String, ResultState<*>>> = MediatorLiveData(HashMap())

    fun transitionState(tag: String, state: ResultState<*>) {

        val map = transition.value ?: return

        map[tag] = state

        transition.postValue(map)
    }

    suspend fun awaitTransition() {

        transition.asFlow().filter { map ->
            map.isNotEmpty() && map.values.all { it.isSuccess() }
        }.first()
    }
}