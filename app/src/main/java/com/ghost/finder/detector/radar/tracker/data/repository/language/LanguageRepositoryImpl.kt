package com.ghost.finder.detector.radar.tracker.data.repository.language

import com.ghost.finder.detector.radar.tracker.data.local.DataStoreManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull

class LanguageRepositoryImpl(
    private val dataStoreManager: DataStoreManager
) : LanguageRepository {
    companion object {
        private const val DEFAULT_LANGUAGE = "en"
    }

    override suspend fun changeLanguage(language: String?): Result<Unit> {
        language?.let {
            dataStoreManager.saveStringData(
                key = DataStoreManager.Companion.KEY_LANGUAGE,
                value = it
            )
        }
        return Result.success(Unit)
    }

    override suspend fun getCurrentLanguage(): Result<String?> = coroutineScope {
        val currentLanguageDeferred =
            async { dataStoreManager.getStringData(DataStoreManager.Companion.KEY_LANGUAGE) }
        try {
            val result = currentLanguageDeferred.await().firstOrNull()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}