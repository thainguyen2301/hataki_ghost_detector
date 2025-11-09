package com.ghost.finder.detector.radar.tracker.data.repository.compass

import kotlinx.coroutines.flow.StateFlow

interface CompassRepository {
    fun startDetectCompass() : Result<Boolean>
    fun stopDetectCompass() : Result<Boolean>

    fun observeCompass(): StateFlow<Float>

    fun observeAccelerometerFlow(): StateFlow<Triple<Float, Float, Float>>

    fun observeMagneticFlow(): StateFlow<Triple<Float, Float, Float>>
}