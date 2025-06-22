package com.tuanha.app

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.simple.coreapp.ui.base.activities.BaseViewBindingActivity
import com.simple.coreapp.ui.dialogs.toast.ToastDialog
import com.simple.coreapp.ui.dialogs.confirm.VerticalConfirmDialogFragment
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.utils.ext.DP
import com.simple.coreapp.utils.ext.RichSpan
import com.simple.coreapp.utils.ext.setText
import com.simple.coreapp.utils.ext.toRich
import com.simple.coreapp.utils.ext.with
import com.simple.coreapp.utils.exts.showOrAwaitDismiss
import com.tuanha.app.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)

        val text = "hello, wellcome to android"
            .with("hello", RichSpan.Bold)
            .with(RichSpan.ForegroundColor(Color.RED))

        binding!!.tvMessage.setText(text)
//        val adapter = MultiAdapter(NoneTextAdapter()).apply {
//
//            setRecyclerView(binding.recyclerView)
//        }
//
//        lifecycleScope.launch {
//
//            delay(1000)
//
//            val list = listOf(
//
//                NoneTextViewItem(
//                    id = "",
//                    text = "text".with(ForegroundColorSpan(Color.BLACK)),
//                    textSize = Size(
//                        width = LayoutParams.MATCH_PARENT,
//                        height = LayoutParams.MATCH_PARENT
//                    ),
//                    textPadding = Padding(
//                        top = DP.DP_8,
//                        bottom = DP.DP_8,
//
//                        left = DP.DP_16,
//                        right = DP.DP_16
//                    ),
//                    textBackground = Background(
//                        cornerRadius = DP.DP_8,
//                        strokeWidth = DP.DP_1,
//                        strokeColor = Color.parseColor("#5b5b5b")
//                    ),
//                    padding = Padding(
//                        top = DP.DP_8,
//                        bottom = DP.DP_8,
//
//                        left = DP.DP_16,
//                        right = DP.DP_16
//                    )
//                ),
//
//                NoneTextViewItem(
//                    id = "1",
//                    text = "text2".with(ForegroundColorSpan(Color.BLACK)),
//                    textSize = Size(
//                        width = LayoutParams.MATCH_PARENT,
//                        height = LayoutParams.MATCH_PARENT
//                    )
//                )
//            )
//            adapter.submitList(list)
//        }
//
//        lifecycleScope.launch {
//
//            delay(5 * 1000)
//
//            HorizontalConfirmDialogFragment.newInstance(
//                isCancel = true,
//
//                keyRequest = "keyRequest",
//
//                anim = com.simple.coreapp.R.raw.img_core_error,
//
//                title = "title",
//                message = "message",
//                background = Background(
//                    backgroundColor = Color.WHITE,
//                    cornerRadius = DP.DP_16
//                ),
//                positive = ButtonInfo(
//                    text = "positive",
//                    background = Background(
//
//                    )
//                ),
//                negative = ButtonInfo(
//                    text = "negative",
//                    background = Background(
//
//                    )
//                )
//            ).showOrAwaitDismiss(supportFragmentManager, "")
//        }

        lifecycleScope.launch {

            delay(7 * 1000)

            ToastDialog.newInstance(
                this@MainActivity,
                message = "message".with(RichSpan.ForegroundColor(Color.RED)),
                background = Background(
                    backgroundColor = Color.WHITE,
                    cornerRadius = DP.DP_16
                ),
            ).showOrAwaitDismiss(supportFragmentManager, "")
        }
    }
}