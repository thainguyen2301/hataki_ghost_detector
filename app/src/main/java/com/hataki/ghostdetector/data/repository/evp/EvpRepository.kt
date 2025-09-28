package com.hataki.ghostdetector.data.repository.evp

import com.hataki.ghostdetector.data.model.EVPData
import kotlinx.coroutines.flow.Flow

interface EvpRepository {
    fun startRecording(): Result<Boolean>
    fun stopRecording(): Result<Boolean>
    fun observeEVP(): Flow<EVPData>
}