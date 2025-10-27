package com.ghostfinder.ghostdetector.radar.ui.rada.system

import android.content.Context
import android.media.SoundPool
import com.ghostfinder.ghostdetector.radar.R

class SoundPoolManager {
    private lateinit var soundPool: SoundPool
    private var beepSoundId: Int = 0

    constructor(context: Context) {
        soundPool = SoundPool.Builder().setMaxStreams(2).build()
        beepSoundId = soundPool.load(context, R.raw.beep, 1)
    }

    fun playBeep() {
        soundPool.play(beepSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun onDestroy() {
        soundPool.release()
    }
}