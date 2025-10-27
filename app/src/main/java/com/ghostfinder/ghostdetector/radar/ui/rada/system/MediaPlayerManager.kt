package com.ghostfinder.ghostdetector.radar.ui.rada.system

import android.content.Context
import android.media.MediaPlayer
import com.ghostfinder.ghostdetector.radar.R

class MediaPlayerManager {
    private var bgMusic: MediaPlayer? = null

    fun playBackgroundMusic(context: Context) {
        if (bgMusic == null) {
            bgMusic = MediaPlayer.create(context, R.raw.bg_sound)
            bgMusic?.isLooping = true
        }
        bgMusic?.start()
    }

    fun stopBackgroundMusic() {
        bgMusic?.pause()
        bgMusic?.seekTo(0)
    }

    fun onDestroy() {
        bgMusic?.release()
        bgMusic = null
    }
}