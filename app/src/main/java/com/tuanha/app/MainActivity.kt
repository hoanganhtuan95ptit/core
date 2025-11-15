package com.tuanha.app

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.autobind.AutoBind
import com.simple.adapter.utils.exts.submitListAwait
import com.simple.coreapp.ui.adapters.texts.NoneTextViewItem
import com.simple.coreapp.utils.ext.ForegroundColor
import com.simple.coreapp.utils.ext.with
import com.simple.detect_2.DetectTask
import com.tuanha.app.databinding.ActivityMainBinding
import com.tuanha.translate_2.TranslateTask
import com.unknown.theme.appTheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        val items = arrayListOf(
            NoneTextViewItem(
                text = "test".with(ForegroundColor(Color.RED))
            )
        )

        lifecycleScope.launch {

            AutoBind.awaitLoaded()

            binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.recyclerView.submitListAwait(items)
        }


        lifecycleScope.launch {

            DetectTask.instant.map { list ->

                list.find { it.isSupport("en") }
            }.collect {

                Log.d("tuanha", "onCreate: DetectTask $it")
            }
        }


        lifecycleScope.launch {

            TranslateTask.instant.map { list ->

                list.find { it.isSupport("en") }
            }.collect {

                Log.d("tuanha", "onCreate: TranslateTask $it")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("tuanha", "onResume: ${System.currentTimeMillis() - App.start}")
    }
}