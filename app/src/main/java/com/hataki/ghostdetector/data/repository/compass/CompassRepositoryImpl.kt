package com.hataki.ghostdetector.data.repository.compass

import com.hataki.ghostdetector.data.framework.compass.CompassManager
import kotlinx.coroutines.flow.StateFlow

class CompassRepositoryImpl(private val compassManager: CompassManager) : CompassRepository {
    override fun startDetectCompass(): Result<Boolean> {
        compassManager.start()
        return Result.success(true)
    }

    override fun stopDetectCompass(): Result<Boolean> {
        compassManager.stop()
        return Result.success(true)
    }

    override fun observeCompass(): StateFlow<Float> {
        return compassManager.azimuthFlow
    }

    override fun observeAccelerometerFlow(): StateFlow<Triple<Float, Float, Float>> {
        return compassManager.accelerometerFlow
    }

    override fun observeMagneticFlow(): StateFlow<Triple<Float, Float, Float>> {
        return compassManager.magneticFlow
    }
}