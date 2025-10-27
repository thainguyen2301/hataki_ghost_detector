package com.ghostfinder.ghostdetector.radar.data.repository.evp

import com.ghostfinder.ghostdetector.radar.data.framework.evp.EVPRecorderManager
import com.ghostfinder.ghostdetector.radar.data.model.EVPData
import kotlinx.coroutines.flow.Flow

class EvpRepositoryImpl(
    private val recorderManager: EVPRecorderManager
) : EvpRepository {
    override fun startRecording(): Result<Boolean> {
        recorderManager.startRecording()
        return Result.success(true)
    }

    override fun stopRecording(): Result<Boolean> {
        recorderManager.stopRecording()
        return Result.success(true)
    }

    override fun observeEVP(): Flow<EVPData> = recorderManager.evpFlow
}