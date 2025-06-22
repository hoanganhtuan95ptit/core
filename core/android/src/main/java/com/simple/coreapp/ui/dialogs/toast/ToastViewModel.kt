package com.simple.coreapp.ui.dialogs.toast

import androidx.lifecycle.ViewModels.BaseViewModel
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.Margin
import com.simple.coreapp.ui.view.Padding
import com.simple.coreapp.utils.ext.RichText
import java.util.concurrent.ConcurrentHashMap

class ToastViewModel : BaseViewModel() {

    val infoMap = ConcurrentHashMap<Id, ToastInfo>()

    fun updateInfo(
        id: String,

        image: Int = 0,
        imageClose: Int = 0,

        message: RichText? = null,

        margin: Margin? = null,
        padding: Padding? = null,
        background: Background? = null
    ) {

        infoMap[Id(id)] = ToastInfo(
            image = image,
            imageClose = imageClose,

            message = message,

            margin = margin,
            padding = padding,
            background = background,
        )
    }

    data class Id(
        val id: String,
    )

    data class ToastInfo(
        val image: Int = 0,
        val imageClose: Int = 0,

        val message: RichText? = null,

        val margin: Margin? = null,
        val padding: Padding? = null,
        val background: Background? = null
    )
}