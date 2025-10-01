package com.hataki.ghostdetector.data.repository.language

interface LanguageRepository {
    suspend fun changeLanguage(language: String?): Result<Unit>
    suspend fun getCurrentLanguage(): Result<String?>
}