package com.hataki.ghostdetector.data.repository.evp

import com.hataki.ghostdetector.data.framework.evp.EVPRecorderManager
import com.hataki.ghostdetector.data.model.EVPData
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