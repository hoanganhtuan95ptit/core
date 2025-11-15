package com.tuanha.translate_2.mlkit

import android.app.Application
import androidx.lifecycle.MediatorLiveData

internal object MlkitTranslate {

    val applicationFlow = MediatorLiveData<Application>()
}