package com.simple.coreapp.ui.base.fragments.transition

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.view.doOnPreDraw
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.ViewModel
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.simple.coreapp.TRANSITION
import com.simple.coreapp.TRANSITION_DURATION
import com.simple.coreapp.ui.base.fragments.BaseViewModelFragment
import com.simple.coreapp.utils.ext.getViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val module = module {
    viewModel { BaseTransitionViewModel() }
}

private val loadKoinModules by lazy {
    loadKoinModules(module)
}

private fun injectModule() = loadKoinModules

open class BaseTransitionFragment<T : ViewBinding, VM : ViewModel>(@LayoutRes contentLayoutId: Int = 0) : BaseViewModelFragment<T, VM>(contentLayoutId) {

    open var isSupportTransition: Boolean = true

    private val transitionViewModel: BaseTransitionViewModel by lazy {
        getViewModel(this, BaseTransitionViewModel::class)
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
