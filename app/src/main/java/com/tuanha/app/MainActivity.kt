package com.tuanha.app

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.adapter.utils.exts.submitListAwait
import com.simple.coreapp.ui.adapters.texts.NoneTextViewItem
import com.simple.coreapp.utils.ext.ForegroundColor
import com.simple.coreapp.utils.ext.with
import com.tuanha.app.databinding.ActivityMainBinding
import com.unknown.theme.appTheme
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        lifecycleScope.launch {

            val items = arrayListOf(
                NoneTextViewItem(
                    text = "test".with(ForegroundColor(Color.RED))
                )
            )

            binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.recyclerView.submitListAwait(items)
        }
        
        lifecycleScope.launch {
            
            appTheme.collect {

                Log.d("tuanha", "onCreate: ${it}")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("tuanha", "onResume: ${System.currentTimeMillis() - App.start}")
    }
}