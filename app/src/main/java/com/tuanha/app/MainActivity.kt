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
import com.one.word.ui.WordDetailFragment
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

            val path = "https://lh3.googleusercontent.com/jLlhxWVvGod_soN5aWDOZCDekFWkKnudzouQdU_f9jukkjFkbUYSZrKg_Nwf3kdy9QVuf4IlKMJti6jm73UjyWGEbyOdhh7IeJDOi5yIA907c0r_n8uqNeiTTbjtcv6Y8ar-sPUuLheCYCQOta0uaUD8C_MvyTmThnQvxeimGG0RqtIGfhOQ7IdTNK7lAkKcLeqJN-2xgEhh3OSlnhx40py7WRZgdb0UUhfRbjnfTYDM8rM_m-xhY_FjBHmj7Fzz8_W54CR3OKcAkknqhH-PKFLlAOD34GfmwrvCHFq458t7cdvThXT0YEm3O1v7o_sURG2HlVBg_c0IMZheM56sg9VM4UG36EgVfpcve2qylMzJAGcut-lYE4vouOU95LYlhnB9RrXN_MWeqVQ7NXni2-J877zQXXQCM5qWW8vkeW2uLUOGSvzWgk0FJKo0yT0FvSsWDdvhkrMGfr2Rta9v4CpZVKWNyThlDpSi_GvtE_wtnmw1LcX8JSHGPoJZOkKwDts6_PHgvolcR6EanC2ToFBhdr2aAcc5yQ5AP67vDhS4hihPoOn3PrzbkJBO_l5wDW6gl4UvIjHmAIsHmzxwNwMAWQGyyQgDVzFYC2htnhbg8cc0JHI6wQhR_zmUyoMMpTWGcSA0YUgRtYV7RuHH36g7vaCvAk_NJIuJ7BrCGmWERTWBrzF1EdmqzWT-1GBvMQDzNDDXUe-nefBghSQ15ebDaiDu2WyTzqcMmOAo_l0P-NeMDOPVpxlUNM7K2EM4Am0Fs5PLa11KTIWaoc_4rdCs5rzpHCgVWmX7pEhkCEE5Tb8G-asXHmErEzQSKlTLwl5AAxKf54VqLpEacOAIt2TmSE3SazcASf-DxFIjlAtL22s-jY3Aw4LWKuK8_77XGJh-GEx0n0rhdEzBHnV0zv5bIKXGKBVQWX6QW1pY-2u54bWG_k8MQ2HFEek2cuw9DHnYQWz8AtB4shMDUNDk2mgn3EBJ9BdtTlFg_3UhJC6VSNd-fD0dNyMDafDCi1c9o23HUVNaFqsJTit15bmKnthsYLLpOJl16JjdT7gslUy-dE0f9fsm6s20DvEQVdCj86WzYrh_8T25tT5J58LcVdpFckayN8MeTKn6GgPPs0rPZsVoJ-r_TK8jJE41H-r5iSaweA=w786-h220-no?authuser=1"


            val sizeMax = 3000


            detectUseCase.execute(DetectUseCase.Param(path = path, "en", "en", DetectOption.TEXT_TRANSLATE, sizeMax)).let { state ->

                state.doSuccess { list ->

                    lifecycleScope.launch(Dispatchers.Main) {

                        findViewById<ImageView>(R.id.image).setImage(path, FitCenter(), DrawTextTransformation(sizeMax, list))
                    }

                    list.forEach {
                        Log.d("tuanha", "onCreate: detectUseCase languageCode:${it.languageCode} text:${it.text}")
                    }
                }

                state.doFailed {

                    Log.d("tuanha", "onCreate: detectUseCase", it)
                }
            }
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