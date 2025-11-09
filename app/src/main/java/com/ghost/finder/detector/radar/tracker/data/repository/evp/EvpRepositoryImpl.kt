package com.ghost.finder.detector.radar.tracker.data.repository.evp

import com.ghost.finder.detector.radar.tracker.data.framework.evp.EVPRecorderManager
import com.ghost.finder.detector.radar.tracker.data.model.EVPData
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