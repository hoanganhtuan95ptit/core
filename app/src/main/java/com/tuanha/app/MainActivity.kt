package com.tuanha.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

//    private val translateUseCase: TranslateUseCase by lazy {
//        getKoin().get()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        lifecycleScope.launchWhenResumed {
//            translateUseCase.execute(TranslateUseCase.Param("Xin chào bạn", "vi", "en")).collect{
//                Log.d("tuanha", "onCreate: $it")
//            }
//        }
    }
}