package com.voicechanger.effect.changevoice.ui.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.base.RegionHatakiHelper
import com.ghostfinder.ghostdetector.radar.ads.model.LanguageHatakiItem
import com.ghostfinder.ghostdetector.radar.ads.model.LanguageHatakiModel
import com.ghostfinder.ghostdetector.radar.ads.model.RegionHatakiModel
import com.ghostfinder.ghostdetector.radar.ui.base.BaseViewModel


class HatakiLanguageViewModel : BaseViewModel() {
    private val _language = MutableLiveData<List<LanguageHatakiItem>>()
    val language: LiveData<List<LanguageHatakiItem>> get() = _language

    private val _selectedRegion = MutableLiveData("")
    val selectedRegion: LiveData<String> get() = _selectedRegion.distinctUntilChanged()

    private val languageData = mutableListOf<LanguageHatakiModel>()


    fun getLanguageList(
        screenOpenCount: Int = 2,
        selectedCode: String? = null,
        selectedRegionName: String? = null,
    ) {
        val listLang = getAllLangWithExtraRegion()
        val listLanguage = setupListLang(
            screenOpenCount,
            selectedCode,
            selectedRegionName,
            listLang
        )
        getLanguageItemData(listLanguage)
    }

    fun selectLang(model: LanguageHatakiItem) {
        when (model) {
            is LanguageHatakiItem.ParentWithoutChild -> selectParentWithoutChild(model)
            is LanguageHatakiItem.Parent -> selectParent(model)
            is LanguageHatakiItem.Child -> selectChild(model)
        }
        if (model !is LanguageHatakiItem.Parent) {
            _selectedRegion.value = model.getItemId()
        }
    }

    fun getSelectedCode() = getSelectedItem()?.code

    fun getSelectedRegionName(): String? {
        val selectedItem = getSelectedItem()
        return if (selectedItem?.mainRegion?.isSelected == true) {
            selectedItem.mainRegion.regionName
        } else {
            selectedItem?.extraRegion?.firstOrNull { it.isSelected }?.regionName
        }
    }

    private fun selectParentWithoutChild(model: LanguageHatakiItem.ParentWithoutChild) {
        val newLangList = languageData.map { language ->
            val mainRegion = language.mainRegion.copy(
                isSelected = language.id == model.id
            )
            language.copy(
                isShowAnimation = false,
                mainRegion = mainRegion,
                extraRegion = language.extraRegion.map { it.copy(isSelected = false) }
            )
        }
        getLanguageItemData(newLangList)
    }

    private fun selectParent(model: LanguageHatakiItem.Parent) {
        val newLangList = languageData.map { language ->
            language.copy(
                isShowAnimation = false,
                isExpanded = if (language.id == model.id) language.isExpanded.not() else language.isExpanded,
            )
        }
        getLanguageItemData(newLangList)
    }

    private fun selectChild(model: LanguageHatakiItem.Child) {
        val newLangList = languageData.map { language ->
            language.copy(
                isShowAnimation = false,
                mainRegion = language.mainRegion.copy(isSelected = false),
                extraRegion = language.extraRegion.map { region ->
                    region.copy(isSelected = region.id == model.id)
                }
            )
        }
        getLanguageItemData(newLangList)
    }

    private fun getLanguageItemData(originalData: List<LanguageHatakiModel>) {
        languageData.clear()
        languageData.addAll(originalData)
        _language.value = originalData.flatMap { language ->
            language.toItemModel()
        }
    }

    private fun getSelectedItem() = languageData.firstOrNull { language ->
        language.mainRegion.isSelected || language.extraRegion.any { it.isSelected }
    }


    private fun setupListLang(
        screenOpenCount: Int = 2,
        selectedCode: String? = null,
        selectedRegionName: String? = null,
        listLang: MutableList<LanguageHatakiModel>,
    ): List<LanguageHatakiModel> {

        val posHandAnimate = getHandPosition(screenOpenCount)
        val selectedPos = listLang.indexOfFirst { it.code == selectedCode }

        return if (selectedCode != null && selectedPos >= 0) {
            val selectedLang = listLang[selectedPos]
            if (selectedLang.extraRegion.isEmpty()) {
                val mainRegion = selectedLang.mainRegion.copy(isSelected = true)
                listLang[selectedPos] = selectedLang.copy(mainRegion = mainRegion)
            } else {
                val extraRegion = selectedLang.extraRegion.map {
                    it.copy(isSelected = it.regionName == selectedRegionName)
                }
                listLang[selectedPos] = selectedLang.copy(
                    isExpanded = true,
                    extraRegion = extraRegion
                )
            }
            getListLangSorted(listLang)
        } else {
            getListLangWithHand(posHandAnimate, listLang)
        }
    }

    private fun getAllLang() = listOf(
        LanguageHatakiModel("English", "en",
            RegionHatakiModel("English", R.drawable.ic_language_en)
        ),
        LanguageHatakiModel("Spanish", "es", RegionHatakiModel("Española", R.drawable.ic_language_es)),
        LanguageHatakiModel("Portuguese", "pt", RegionHatakiModel("Português", R.drawable.ic_language_pt)),
        LanguageHatakiModel("Japanese", "ja", RegionHatakiModel("日本語", R.drawable.ic_language_jp)),
        LanguageHatakiModel("Korean", "ko", RegionHatakiModel("한국인", R.drawable.ic_language_ko)),
        LanguageHatakiModel("Hindi", "hi", RegionHatakiModel("हिन्दी", R.drawable.ic_language_hi)),
        LanguageHatakiModel("French", "fr", RegionHatakiModel("Français", R.drawable.ic_language_fr)),
        LanguageHatakiModel("German", "de", RegionHatakiModel("Deutsch", R.drawable.ic_language_de)),
    )

    private fun getAllLangWithExtraRegion(): MutableList<LanguageHatakiModel> {
        return getAllLang().map {
            it.copy(extraRegion = RegionHatakiHelper.getRegionsByLanguage(it.code))
        }.toMutableList()
    }

    private fun getHandPosition(screenOpenCount: Int): Int {
        return if (screenOpenCount <= 1) DEFAULT_POSITION_HAND_ANIMATE else INVALID_POSITION
    }

    private fun getListLangSorted(listLang: MutableList<LanguageHatakiModel>): List<LanguageHatakiModel> {
        val sortedList = listLang.sortedBy { language ->
            language.mainRegion.isSelected || language.extraRegion.any { it.isSelected }
        }
        return sortedList.reversed().toList()
    }

    private fun getListLangWithHand(
        posHandAnimate: Int,
        listLang: MutableList<LanguageHatakiModel>,
    ): List<LanguageHatakiModel> {
        if (posHandAnimate > 0 && posHandAnimate <= listLang.size) {
            listLang[posHandAnimate - 1] = listLang[posHandAnimate - 1].copy(isShowAnimation = true)
        }
        return listLang.toList()
    }

    companion object {
        private const val DEFAULT_POSITION_HAND_ANIMATE = 2
        private const val INVALID_POSITION = -1
    }
}