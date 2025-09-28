package com.hataki.ghostdetector.data.repository.sensor

import com.hataki.ghostdetector.data.framework.sensor.SensorDetectManager
import kotlinx.coroutines.flow.Flow

class SensorRepositoryImpl(private val sensorManager: SensorDetectManager) : SensorRepository {
    override fun observeEmf(): Flow<Triple<Float, Float, Float>> = sensorManager.emfFlow

    override fun startDetectSensor(): Result<Boolean> {
        sensorManager.start()
        return Result.success(true)
    }

    override fun stopDetectSensor(): Result<Boolean> {
        sensorManager.stop()
        return Result.success(true)
    }
}