package com.ghostfinder.ghostdetector.radar.data.repository.evp

import com.ghostfinder.ghostdetector.radar.data.model.EVPData
import kotlinx.coroutines.flow.Flow

interface EvpRepository {
    fun startRecording(): Result<Boolean>
    fun stopRecording(): Result<Boolean>
    fun observeEVP(): Flow<EVPData>
}