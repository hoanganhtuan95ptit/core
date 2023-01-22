package com.tuanha.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.one.coreapp.data.usecase.doFailed
import com.one.coreapp.data.usecase.doSuccess
import com.one.detect.data.usecase.DetectUseCase
import com.one.translate.data.usecase.TranslateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin

class MainActivity : AppCompatActivity() {

    private val detectUseCase: DetectUseCase by lazy {
        getKoin().get()
    }

    private val translateUseCase: TranslateUseCase by lazy {
        getKoin().get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("tuanha", "onCreate: detectUseCase start")

            detectUseCase.execute(DetectUseCase.Param("https://images.hindustantimes.com/img/2022/12/30/original/newyear2023c_1672394271188.jpg", "vi", "en")).let { state ->

                state.doSuccess { list ->

                    list.forEach {
                        Log.d("tuanha", "onCreate: detectUseCase ${it.text}")
                    }
                }

                state.doFailed {

                    Log.d("tuanha", "onCreate: detectUseCase", it)
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("tuanha", "onCreate: translateUseCase start")

            translateUseCase.execute(TranslateUseCase.Param(listOf("Chúc mừng năm mới", "Thành công", "Sức khỏe", "May mắn", "Hạnh phúc"), "vi", "en")).let { state ->

                state.doSuccess { list ->

                    list.forEach {
                        Log.d("tuanha", "onCreate: translateUseCase $it")
                    }
                }

                state.doFailed {

                    Log.d("tuanha", "onCreate: translateUseCase ", it)
                }
            }
        }
    }
}