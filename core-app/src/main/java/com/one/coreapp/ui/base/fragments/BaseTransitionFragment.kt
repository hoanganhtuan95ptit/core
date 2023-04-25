package com.one.coreapp.ui.fragment.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.view.doOnPreDraw
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.one.coreapp.TRANSITION
import com.one.coreapp.TRANSITION_DURATION
import com.one.coreapp.ui.base.fragments.BaseViewBindingFragment
import com.one.coreapp.ui.base.viewmodels.BaseViewModel
import com.one.coreapp.utils.extentions.Event
import com.one.coreapp.utils.extentions.postDifferentValue
import com.one.coreapp.utils.extentions.toEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val module = module {
    viewModel { BaseTransitionViewModel() }
}

private val loadKoinModules by lazy {
    loadKoinModules(module)
}

private fun injectModule() = loadKoinModules

open class BaseTransitionFragment<T : ViewBinding>(@LayoutRes contentLayoutId: Int = 0) : BaseViewBindingFragment<T>(contentLayoutId) {

    open var isSupportTransition: Boolean = true

    private val transitionViewModel: BaseTransitionViewModel by lazy {
        getKoin().getViewModel(this, BaseTransitionViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectModule()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isSupportTransition) {

            view.transitionName = arguments?.getString(TRANSITION, null) ?: TRANSITION

            onCreateTransition()
        } else {

            transitionViewModel.animationEnd()
        }

        observeTransitionData()
    }

    private fun onCreateTransition() {

        exitTransition = createExitTransition()?.addListener(object : Transition.TransitionListener {

            override fun onTransitionStart(transition: Transition) {
                transitionViewModel.animationStart()
            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                transitionViewModel.animationEnd()
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionResume(transition: Transition) {
            }

        })

        returnTransition = createReturnTransition()

        sharedElementEnterTransition = createEnterTransition()?.addListener(object : Transition.TransitionListener {

            override fun onTransitionStart(transition: Transition) {
                transitionViewModel.animationStart()
            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                transitionViewModel.animationEnd()
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionResume(transition: Transition) {
            }

        })
    }

    private fun observeTransitionData() = with(transitionViewModel) {

        viewReady.observe(viewLifecycleOwner) {

            onViewReady()
        }

        transitionEndEvent.observe(viewLifecycleOwner) { event ->

            event.getContentIfNotHandled()?.takeIf { it } ?: return@observe

            onTransitionEnd()
        }

        view?.doOnPreDraw {
            awaitViewReady()
        }
    }

    @CallSuper
    protected open fun onViewReady() {
    }

    @CallSuper
    protected open fun onTransitionEnd() {
    }

    open fun createExitTransition(): Transition? = MaterialElevationScale(false).apply {

        duration = TRANSITION_DURATION
    }

    open fun createReturnTransition(): Transition = Slide().apply {

        duration = TRANSITION_DURATION
    }

    open fun createEnterTransition(): Transition? = MaterialContainerTransform(requireContext(), true).apply {

        duration = TRANSITION_DURATION

        fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH

        setPathMotion(MaterialArcMotion())

        interpolator = FastOutSlowInInterpolator()
        scrimColor = Color.TRANSPARENT
    }
}

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