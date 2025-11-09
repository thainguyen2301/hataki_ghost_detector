package com.ghost.finder.detector.radar.tracker.ui.rada.system

import android.content.Context
import android.media.MediaPlayer
import com.ghost.finder.detector.radar.tracker.R

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