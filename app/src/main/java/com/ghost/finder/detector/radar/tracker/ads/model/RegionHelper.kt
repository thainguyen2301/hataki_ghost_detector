package com.ghost.finder.detector.radar.tracker.ads.model

import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.data.model.RegionModel
import kotlin.text.lowercase

object RegionHelper {
    fun getRegionsByLanguage(languageCode: String): List<RegionModel> {
        return when (languageCode.lowercase()) {
            "en" -> getEnglishRegions()
            "es" -> getSpanishRegions()
            "ja" -> getJapaneseRegions()
            "ko" -> getKoreanRegions()
            "pt" -> getPortugueseRegions()
            else -> emptyList()
        }
    }

    private fun getEnglishRegions() = listOf(
        RegionModel("American", R.drawable.ic_language_us),
        RegionModel("British", R.drawable.ic_language_en),
        RegionModel("Indian", R.drawable.ic_language_hi),
    )

    private fun getSpanishRegions() = listOf(
        RegionModel("Spanish", R.drawable.ic_language_es),
        RegionModel("Mexican", R.drawable.ic_language_mexico),
    )

    private fun getJapaneseRegions() = listOf(
        RegionModel("Japanese", R.drawable.ic_language_jp),
    )

    private fun getKoreanRegions() = listOf(
        RegionModel("Korean", R.drawable.ic_language_ko),
    )

    private fun getPortugueseRegions() = listOf(
        RegionModel("Portuguese", R.drawable.ic_language_pt),
        RegionModel("EU", R.drawable.ic_language_eu),
        RegionModel("Brazin", R.drawable.ic_language_brazin),
    )
}