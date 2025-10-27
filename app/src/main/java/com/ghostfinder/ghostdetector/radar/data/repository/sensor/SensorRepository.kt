package com.ghostfinder.ghostdetector.radar.data.repository.sensor

import kotlinx.coroutines.flow.Flow

interface SensorRepository {
    fun observeEmf(): Flow<Triple<Float, Float, Float>>
    fun startDetectSensor(): Result<Boolean>
    fun stopDetectSensor(): Result<Boolean>
}