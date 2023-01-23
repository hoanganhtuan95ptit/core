package com.tuanha.app

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.one.coreapp.data.usecase.doFailed
import com.one.coreapp.data.usecase.doSuccess
import com.one.coreapp.utils.extentions.setImage
import com.one.coreapp.utils.extentions.toBitmap
import com.one.detect.data.usecase.DetectUseCase
import com.one.detect.entities.DetectOption
import com.one.translate.data.usecase.TranslateUseCase
import com.tuanha.app.utils.DrawTextTransformation
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

            val path = "https://images.hindustantimes.com/img/2022/12/30/original/newyear2023c_1672394271188.jpg"


            val bitmap = path.toBitmap()


            detectUseCase.execute(DetectUseCase.Param(path = path, "vi", "en", DetectOption.TEXT_TRANSLATE)).let { state ->

                state.doSuccess { list ->

                    lifecycleScope.launch(Dispatchers.Main) {

                        findViewById<ImageView>(R.id.image).setImage(path, FitCenter(), DrawTextTransformation(maxOf(bitmap.width, bitmap.height), list))
                    }

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