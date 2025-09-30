package com.hataki.ghostdetector.data.repository.language

import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    suspend fun changeLanguage(language: String?): Result<Unit>
    suspend fun getCurrentLanguage(): Flow<String>
}