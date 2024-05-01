package com.simple.coreapp.ui.base.fragments.transition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.simple.coreapp.ui.base.viewmodels.BaseViewModel
import com.simple.coreapp.utils.extentions.Event
import com.simple.coreapp.utils.extentions.postDifferentValue
import com.simple.coreapp.utils.extentions.toEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BaseTransitionViewModel : BaseViewModel() {

    var jobViewReady: Job? = null

    val viewReady: LiveData<Boolean> = MediatorLiveData()


    var jobTransitionEnd: Job? = null

    var transitionEnd: LiveData<Boolean> = MediatorLiveData()

    val transitionEndEvent: LiveData<Event<Boolean>> = transitionEnd.toEvent()


    fun awaitViewReady() {

        jobViewReady = viewModelScope.launch(handler + Dispatchers.IO) {

            delay(300)
            viewReady.postDifferentValue(true)
        }.apply {

        }

        jobTransitionEnd = viewModelScope.launch(handler + Dispatchers.IO) {

            delay(300)
            transitionEnd.postDifferentValue(true)
        }.apply {

            transitionEnd.postDifferentValue(false)
        }
    }

    fun animationStart() {

        jobViewReady?.cancel()

        jobTransitionEnd?.cancel()
    }

    fun animationEnd() {

        jobViewReady?.cancel()
        viewReady.postDifferentValue(true)

        jobTransitionEnd?.cancel()
        transitionEnd.postDifferentValue(true)
    }
}