package androidx.lifecycle.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.simple.coreapp.utils.extentions.Enable
import com.simple.coreapp.utils.extentions.postDifferentValue
import com.simple.coreapp.utils.extentions.toEnable
import org.koin.core.component.KoinComponent

abstract class BaseViewModel : ViewModel(), KoinComponent {

    val uiReady: LiveData<Boolean> = MediatorLiveData()

    val uiVisibleEnable: LiveData<Enable> = uiReady.toEnable()

    fun updateUiReady(uiReady: Boolean) {
        this.uiReady.postDifferentValue(uiReady)
    }
}
