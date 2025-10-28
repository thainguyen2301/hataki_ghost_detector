package com.ghostfinder.ghostdetector.radar.ads.model

import java.util.UUID

sealed class LanguageHatakiItem {
    data class Parent(
        val languageName: String,
        val image: Int,
        val regionName: String,
        val flags: List<Int>,
        val isShowAnimation: Boolean = false,
        val id: String = UUID.randomUUID().toString()
    ) : LanguageHatakiItem()

    data class ParentWithoutChild(
        val languageName: String,
        val code: String,
        val image: Int,
        val regionName: String,
        val isSelected: Boolean = false,
        val isShowAnimation: Boolean = false,
        val id: String = UUID.randomUUID().toString()
    ) : LanguageHatakiItem()

    data class Child(
        val regionName: String,
        val code: String,
        val image: Int,
        val isSelected: Boolean = false,
        val id: String = UUID.randomUUID().toString()
    ) : LanguageHatakiItem()

    fun getItemId(): String {
        return when (this) {
            is Parent -> id
            is ParentWithoutChild -> id
            is Child -> id
        }
    }
}
