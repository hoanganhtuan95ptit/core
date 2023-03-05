package com.tuanha.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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

//            val path = "https://www.apkonline.net/imagescropped/viewinhindiicon128.jpgplus.webp"
//
//
//            val sizeMax = 1500
//
//
//            detectUseCase.execute(DetectUseCase.Param(path = path, "en", "en", DetectOption.TEXT_TRANSLATE, sizeMax)).let { state ->
//
//                state.doSuccess { list ->
//
//                    lifecycleScope.launch(Dispatchers.Main) {
//
//                        findViewById<ImageView>(R.id.image).setImage(path, FitCenter(), DrawTextTransformation(sizeMax, list))
//                    }
//
//                    list.forEach {
//                        Log.d("tuanha", "onCreate: detectUseCase languageCode:${it.languageCode} text:${it.text}")
//                    }
//                }
//
//                state.doFailed {
//
//                    Log.d("tuanha", "onCreate: detectUseCase", it)
//                }
//            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
//            Log.d("tuanha", "onCreate: translateUseCase start")
//
//            translateUseCase.execute(TranslateUseCase.Param(listOf("Chúc mừng năm mới", "Thành công", "Sức khỏe", "May mắn", "Hạnh phúc"), "vi", "en")).let { state ->
//
//                state.doSuccess { list ->
//
//                    list.forEach {
//                        Log.d("tuanha", "onCreate: translateUseCase $it")
//                    }
//                }
//
//                state.doFailed {
//
//                    Log.d("tuanha", "onCreate: translateUseCase ", it)
//                }
//            }
        }

        lifecycleScope.launchWhenResumed {

//            WordDetailFragment.newInstance("hello", "").show(supportFragmentManager, "")
        }
    }
}