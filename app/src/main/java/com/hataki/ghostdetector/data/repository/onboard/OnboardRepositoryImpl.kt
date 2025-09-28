package com.hataki.ghostdetector.data.repository.onboard

import com.hataki.ghostdetector.data.local.DataStoreManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OnboardRepositoryImpl(private val dataStoreManager: DataStoreManager) : OnboardRepository {
    override suspend fun saveIsOnBoarding(isOnboard: Boolean): Result<Boolean> {
        dataStoreManager.saveBoolData(isOnboard, DataStoreManager.IS_ONBOARDING)
        return Result.success(true)
    }

    override suspend fun getIsOnBoarding(): Flow<Boolean?> = coroutineScope {
        try {
            async {
                dataStoreManager.getBoolData(DataStoreManager.IS_ONBOARDING)
            }.await()
        } catch (e: Exception) {
            flow { emit(false) }
        }
    }
}