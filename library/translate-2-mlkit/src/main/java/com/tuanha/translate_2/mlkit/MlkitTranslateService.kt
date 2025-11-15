package com.tuanha.translate_2.mlkit

import android.app.Application
import com.simple.autobind.annotation.AutoBind
import com.simple.service.ApplicationService

@AutoBind(ApplicationService::class)
class MlkitTranslateService : ApplicationService {

    override fun setup(application: Application) {
        MlkitTranslate.applicationFlow.postValue(application)
    }
}