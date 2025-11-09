package com.ghost.finder.detector.radar.tracker.data.repository.language

interface LanguageRepository {
    suspend fun changeLanguage(language: String?): Result<Unit>
    suspend fun getCurrentLanguage(): Result<String?>
}