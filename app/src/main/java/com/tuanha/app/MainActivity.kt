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
import com.one.detect.data.usecase.DetectUseCase
import com.one.detect.entities.DetectOption
import com.one.translate.data.usecase.TranslateUseCase
import com.tuanha.app.utils.AlphaTransformation
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
//            Log.d("tuanha", "onCreate: detectUseCase start")
//
//            val path =
//                "https://lh3.googleusercontent.com/MqoCrLcw3ACoQcVFOjyuVaMeYP5dFL29uluY0A4b_XhyBzF_jwxUUNNwH6svnL3uLQwc63GkxQrW50iDfgBEGJkw3eGaQPg7uvdbJhJrkC3HwC_lMJdyzqd9_UA7MKhSAjGOSplu37BA2k_0QdYYKun6B5nHbNr_wPw4tUdNDLGGw5AfO0vvM5GhgDoPPrbVMW3Fe1YZbrJ2drjniVNHWtIdrnKlAhcgdEp2k_kKJXg4PpUVWSuUP1KhUGrA0fCqsvvfMs41OnxVCe9Y80AYKuo-Y-jG6i0tZlEJ_tPrSAtdLwaFmOVMdEwxkC8JmNy4bB9aQm0eMMLuZQgRIYaNdVTOPkVK6xC2RF8dnZK23P16jfyLbf8a5eqiZDfeFenWxeGtfxug5xvj3DA9RikwcnkYSXPxYpov94dlePKGctpbJuagbxoUaFQCEvWntdXw7TTvU1hWANdGkhC0mVwCVB9aOOrXOZ5YJ--rkl_E4oJX9Gb4XljZ3FVdY5mgOg75bn7v2QjpEhcI5i_H0ze8mq45lJYDhuAGUMaPtFUEon0qu1r5b8LjjL2iCIUPyoeq8ANVskqLlEOs9Rrgv18kVJ5WLMz7OlvNFIa0v-S1k2a2yxGtTrL4n-eggBtcWaMglaLUdxVfVFAqc0J-FISVAUZ1_p2H7-zvuUa46sUT59NKTCW3dkOfiNJjI_yZRbOSVosDPLewJ78F24ukgAY89YlhJRy406jhZSTkPQgORMqv6ld2QeJ4cNMybbRx0DwNfSkDRxEvRnUvh7xxAaEE_-yWx7LMNroPnWpn6PPmX1bFk2cXmFvQB9qasYp6PcS9_uJSwGPYHrWIgdu0X858c1j1FxcbZ8pUdP2IQRCA0AM3dgS_YrYn60dcFKSrm9Hkmzg6afK-lZrU2X2yagJwvkiJ3YmwPNm52n7xEd5_0jP9ymFLjpnfdTESHFRvXWl1RHZUaQfLDDgBQff_ikpVIAv9v0qKB3E0f2cO-bibDZDUB3v6qwGjq1HykTO1tvuPnoi0tRubt_AowcFMdt21UToIR8eV20GY7SqwU_wgmTHC0HRZzYZchXNlNMfxMpW1t6-Np8AoNfJ3cmhQnj-DGh3m4O0QlwTeoTLDGUaNriWb0ILYospjT0nhOWb-Vbk6-_bbAw=w1194-h812-no?authuser=1p"
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
//                        findViewById<ImageView>(R.id.image).setImage(path, FitCenter(), AlphaTransformation(150), DrawTextTransformation(sizeMax, list))
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