package com.tuanha.app

import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.ViewGroup.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.simple.adapter.MultiAdapter
import com.simple.coreapp.ui.adapters.texts.NoneTextAdapter
import com.simple.coreapp.ui.adapters.texts.NoneTextViewItem
import com.simple.coreapp.ui.base.activities.BaseViewBindingActivity
import com.simple.coreapp.ui.dialogs.confirm.HorizontalConfirmDialogFragment
import com.simple.coreapp.ui.dialogs.confirm.VerticalConfirmDialogFragment
import com.simple.coreapp.ui.view.Padding
import com.simple.coreapp.ui.view.Size
import com.simple.coreapp.ui.view.round.Background
import com.simple.coreapp.utils.ext.ButtonInfo
import com.simple.coreapp.utils.ext.DP
import com.simple.coreapp.utils.ext.with
import com.tuanha.app.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = binding ?: return


        val adapter = MultiAdapter(NoneTextAdapter()).apply {

            setRecyclerView(binding.recyclerView)
        }

        lifecycleScope.launch {

            delay(1000)

            val list = listOf(

                NoneTextViewItem(
                    id = "",
                    text = "text".with(ForegroundColorSpan(Color.BLACK)),
                    textSize = Size(
                        width = LayoutParams.MATCH_PARENT,
                        height = LayoutParams.MATCH_PARENT
                    ),
                    textPadding = Padding(
                        top = DP.DP_8,
                        bottom = DP.DP_8,

                        left = DP.DP_16,
                        right = DP.DP_16
                    ),
                    textBackground = Background(
                        cornerRadius = DP.DP_8,
                        strokeWidth = DP.DP_1,
                        strokeColor = Color.parseColor("#5b5b5b")
                    ),
                    padding = Padding(
                        top = DP.DP_8,
                        bottom = DP.DP_8,

                        left = DP.DP_16,
                        right = DP.DP_16
                    )
                ),

                NoneTextViewItem(
                    id = "1",
                    text = "text2".with(ForegroundColorSpan(Color.BLACK)),
                    textSize = Size(
                        width = LayoutParams.MATCH_PARENT,
                        height = LayoutParams.MATCH_PARENT
                    )
                )
            )
            adapter.submitList(list)
        }

        lifecycleScope.launch {

            delay(5 * 1000)

            HorizontalConfirmDialogFragment.newInstance(
                isCancel = true,

                keyRequest = "keyRequest",

                anim = com.simple.coreapp.R.raw.img_core_error,

                title = "title",
                message = "message",
                positive = ButtonInfo(
                    text = "positive",
                    background = Background(

                    )
                ),
                negative = ButtonInfo(
                    text = "negative",
                    background = Background(

                    )
                )
            ).show(supportFragmentManager, "")
        }
    }
}