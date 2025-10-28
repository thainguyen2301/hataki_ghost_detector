package com.ghostfinder.ghostdetector.radar.ads.base

import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.model.RegionHatakiModel


object RegionHatakiHelper {

    fun getRegionsByLanguage(languageCode: String): List<RegionHatakiModel> {
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
        RegionHatakiModel("American", R.drawable.ic_language_us),
        RegionHatakiModel("British", R.drawable.ic_language_en),
        RegionHatakiModel("Indian", R.drawable.ic_language_hi),
    )

    private fun getSpanishRegions() = listOf(
        RegionHatakiModel("Spanish", R.drawable.ic_language_es),
        RegionHatakiModel("Mexican", R.drawable.ic_language_mexico),
    )

    private fun getJapaneseRegions() = listOf(
        RegionHatakiModel("Japanese", R.drawable.ic_language_jp),
    )

    private fun getKoreanRegions() = listOf(
        RegionHatakiModel("Korean", R.drawable.ic_language_ko),
    )

    private fun getPortugueseRegions() = listOf(
        RegionHatakiModel("Portuguese", R.drawable.ic_language_pt),
        RegionHatakiModel("EU", R.drawable.ic_language_eu),
        RegionHatakiModel("Brazin", R.drawable.ic_language_brazin),
    )
}