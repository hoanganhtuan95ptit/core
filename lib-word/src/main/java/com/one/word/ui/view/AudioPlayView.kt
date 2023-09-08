package com.one.word.ui.view

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.one.coreapp.utils.extentions.setVisible
import com.one.word.R

class AudioPlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mediaPlayer: MediaPlayer? = null

    init {
        inflate(context, R.layout.layout_audio, this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        killMediaPlayer();
    }


    fun play(url: String) {

        findViewById<View>(R.id.progress_bar).setVisible(true)

        killMediaPlayer()

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(url)

        mediaPlayer!!.setOnPreparedListener {

            findViewById<View>(R.id.progress_bar).setVisible(false)
            mediaPlayer!!.start()
        }

        mediaPlayer!!.prepareAsync()
    }

    private fun killMediaPlayer() {

        if (mediaPlayer != null) {
            try {
                mediaPlayer!!.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}