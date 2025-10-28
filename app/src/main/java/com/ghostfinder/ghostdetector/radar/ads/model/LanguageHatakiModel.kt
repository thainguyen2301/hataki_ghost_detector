package com.ghostfinder.ghostdetector.radar.ads.model

import java.util.UUID


data class LanguageHatakiModel(
    val languageName: String,
    val code: String,
    val mainRegion: RegionHatakiModel,
    val extraRegion: List<RegionHatakiModel> = listOf(),
    val isExpanded: Boolean = false,
    val isShowAnimation: Boolean = false,
    val id: String = UUID.randomUUID().toString()
) {
    fun toItemModel(): List<LanguageHatakiItem> {
        return if (isExpanded) {
            val child = extraRegion.map { region ->
                LanguageHatakiItem.Child(
                    regionName = region.regionName,
                    code = code,
                    image = region.image,
                    isSelected = region.isSelected,
                    id = region.id
                )
            }
            val result = mutableListOf(getParent())
            result.addAll(child)
            result.toList()
        } else {
            listOf(getParent())
        }
    }

    private fun getParent(): LanguageHatakiItem {
        return if (extraRegion.isEmpty()) {
            LanguageHatakiItem.ParentWithoutChild(
                languageName = languageName,
                code = code,
                image = mainRegion.image,
                regionName = mainRegion.regionName,
                isSelected = mainRegion.isSelected,
                isShowAnimation = isShowAnimation,
                id = id
            )
        } else {
            LanguageHatakiItem.Parent(
                languageName = languageName,
                image = mainRegion.image,
                regionName = mainRegion.regionName,
                flags = extraRegion.map { it.image }.reversed(),
                isShowAnimation = isShowAnimation,
                id = id
            )
        }
    }
}

data class RegionHatakiModel(
    val regionName: String,
    val image: Int,
    val isSelected: Boolean = false,
    val id: String = UUID.randomUUID().toString()
)
