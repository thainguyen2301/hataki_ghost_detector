package com.ghostfinder.ghostdetector.radar.data.framework.evp

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.ghostfinder.ghostdetector.radar.data.model.EVPData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.math.log10
import kotlin.math.sqrt

class EVPRecorderManager {

    private val sampleRate = 44100
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private var audioRecord: AudioRecord? = null
    private var recordingScope: CoroutineScope? = null

    private val _evpFlow = MutableSharedFlow<EVPData>(replay = 1)
    val evpFlow: SharedFlow<EVPData> = _evpFlow

    @SuppressLint("MissingPermission")
    fun startRecording() {
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord?.startRecording()
        recordingScope = CoroutineScope(Dispatchers.Default)

        recordingScope?.launch {
            val buffer = ShortArray(bufferSize)
            while (true) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    val amplitude = buffer.take(read).maxOrNull()?.toFloat() ?: 0f
                    val rms = sqrt(buffer.take(read).map { it * it }.average()).toFloat()
                    val db = if (rms > 0) 20 * log10(rms / 32767f) else -160f

                    _evpFlow.emit(EVPData(amplitude, db))
                }
            }
        }
    }

    fun stopRecording() {
        recordingScope?.cancel()
        recordingScope = null
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }
}
