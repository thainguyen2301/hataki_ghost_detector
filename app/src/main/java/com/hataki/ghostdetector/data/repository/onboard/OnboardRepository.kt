package com.hataki.ghostdetector.data.repository.onboard

import kotlinx.coroutines.flow.Flow

interface OnboardRepository {
    suspend fun saveIsOnBoarding(isOnboard: Boolean): Result<Boolean>
    suspend fun getIsOnBoarding(): Flow<Boolean?>
}