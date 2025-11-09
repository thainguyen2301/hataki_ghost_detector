package com.ghost.finder.detector.radar.tracker.data.repository.evp

import com.ghost.finder.detector.radar.tracker.data.model.EVPData
import kotlinx.coroutines.flow.Flow

interface EvpRepository {
    fun startRecording(): Result<Boolean>
    fun stopRecording(): Result<Boolean>
    fun observeEVP(): Flow<EVPData>
}