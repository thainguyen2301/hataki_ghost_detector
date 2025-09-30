package com.hataki.ghostdetector.data.repository.language

import com.hataki.ghostdetector.data.local.DataStoreManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

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

    override suspend fun getCurrentLanguage(): Flow<String> = coroutineScope {
        val currentLanguageDeferred =
            async { dataStoreManager.getStringData(DataStoreManager.Companion.KEY_LANGUAGE) }
        try {
            currentLanguageDeferred.await().map { it ?: DEFAULT_LANGUAGE }
        } catch (e: Exception) {
            flowOf(DEFAULT_LANGUAGE)
        }
    }
}