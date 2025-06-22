package com.simple.coreapp.ui.dialogs.confirm

import androidx.lifecycle.ViewModels.BaseViewModel
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.utils.ext.ButtonInfo
import com.simple.coreapp.utils.ext.RichText
import java.util.concurrent.ConcurrentHashMap

internal class ConfirmViewModel : BaseViewModel() {

    val infoMap = ConcurrentHashMap<Id, ConfirmInfo>()

    fun updateInfo(
        id: String,
        keyRequest: String,

        anim: Int = 0,
        image: Int = 0,

        anchor: Background? = null,
        background: Background? = null,

        title: RichText? = null,
        message: RichText? = null,

        negative: ButtonInfo? = null,
        positive: ButtonInfo? = null
    ) {

        infoMap[Id(id, keyRequest)] = ConfirmInfo(
            anim = anim,
            image = image,

            anchor = anchor,
            background = background,

            title = title,
            message = message,

            negative = negative,
            positive = positive
        )
    }

    data class Id(
        val id: String,
        val keyRequest: String
    )

    data class ConfirmInfo(
        val anim: Int = 0,
        val image: Int = 0,

        val anchor: Background? = null,
        val background: Background? = null,

        val title: RichText? = null,
        val message: RichText? = null,

        val negative: ButtonInfo? = null,
        val positive: ButtonInfo? = null
    )
}