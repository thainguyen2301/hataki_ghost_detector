package com.ghostfinder.ghostdetector.radar.ui.rada

import com.ghostfinder.ghostdetector.radar.data.repository.compass.CompassRepository
import com.ghostfinder.ghostdetector.radar.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RadaViewModel @Inject constructor(
    private val compassRepository: CompassRepository
) : BaseViewModel() {
    val azimuth: StateFlow<Float> = compassRepository.observeCompass()
    val accelerometer: StateFlow<Triple<Float, Float, Float>> =
        compassRepository.observeAccelerometerFlow()
    val magnetic: StateFlow<Triple<Float, Float, Float>> = compassRepository.observeMagneticFlow()

    fun startDetectCompass() {
        compassRepository.startDetectCompass()
    }

    fun stopDetectCompass() {
        compassRepository.stopDetectCompass()
    }
}