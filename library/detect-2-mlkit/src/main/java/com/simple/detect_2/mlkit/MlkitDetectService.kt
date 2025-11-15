package com.simple.detect_2.mlkit

import android.app.Application
import com.simple.autobind.annotation.AutoBind
import com.simple.service.ApplicationService

@AutoBind(ApplicationService::class)
class MlkitDetectService : ApplicationService {

    override fun setup(application: Application) {
        MlkitDetect.applicationFlow.postValue(application)
    }
}