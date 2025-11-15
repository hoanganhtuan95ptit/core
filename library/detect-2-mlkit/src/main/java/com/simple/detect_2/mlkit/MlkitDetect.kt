package com.simple.detect_2.mlkit

import android.app.Application
import androidx.lifecycle.MediatorLiveData

internal object MlkitDetect {

    val applicationFlow = MediatorLiveData<Application>()
}