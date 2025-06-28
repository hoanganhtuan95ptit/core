package com.tuanha.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.tuanha.app.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(LayoutInflater.from(this)).root)
    }

    override fun onResume() {
        super.onResume()
        Log.d("tuanha", "onResume: ${System.currentTimeMillis() - App.start}")
    }
}