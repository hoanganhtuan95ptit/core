package com.tuanha.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.one.translate.data.usecase.TranslateUseCase
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.getKoin

class MainActivity : AppCompatActivity() {

    private val translateUseCase: TranslateUseCase by lazy {
        getKoin().get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenResumed {
            translateUseCase.execute(TranslateUseCase.Param("Xin chào bạn", "vi", "en")).collect{
                Log.d("tuanha", "onCreate: $it")
            }
        }
    }
}