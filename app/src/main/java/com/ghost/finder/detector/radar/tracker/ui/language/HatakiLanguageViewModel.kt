package com.ghost.finder.detector.radar.tracker.ui.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.ads.model.RegionHelper
import com.ghost.finder.detector.radar.tracker.data.model.LanguageItem
import com.ghost.finder.detector.radar.tracker.data.model.LanguageModel
import com.ghost.finder.detector.radar.tracker.data.model.RegionModel
import com.ghost.finder.detector.radar.tracker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HatakiLanguageViewModel @Inject constructor() : BaseViewModel() {
    private val _language = MutableLiveData<List<LanguageItem>>()
    val language: LiveData<List<LanguageItem>> get() = _language

    private val _selectedRegion = MutableLiveData("")
    val selectedRegion: LiveData<String> get() = _selectedRegion.distinctUntilChanged()

    private val languageData = mutableListOf<LanguageModel>()


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

    fun selectLang(model: LanguageItem) {
        when (model) {
            is LanguageItem.ParentWithoutChild -> selectParentWithoutChild(model)
            is LanguageItem.Parent -> selectParent(model)
            is LanguageItem.Child -> selectChild(model)
        }
        if (model !is LanguageItem.Parent) {
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

    private fun selectParentWithoutChild(model: LanguageItem.ParentWithoutChild) {
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

    private fun selectParent(model: LanguageItem.Parent) {
        val newLangList = languageData.map { language ->
            language.copy(
                isShowAnimation = false,
                isExpanded = if (language.id == model.id) language.isExpanded.not() else language.isExpanded,
            )
        }
        getLanguageItemData(newLangList)
    }

    private fun selectChild(model: LanguageItem.Child) {
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

    private fun getLanguageItemData(originalData: List<LanguageModel>) {
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
        listLang: MutableList<LanguageModel>,
    ): List<LanguageModel> {

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
        LanguageModel("English", "en", RegionModel("English", R.drawable.ic_language_en)),
        LanguageModel("Spanish", "es", RegionModel("Española", R.drawable.ic_language_es)),
        LanguageModel("Portuguese", "pt", RegionModel("Português", R.drawable.ic_language_pt)),
        LanguageModel("Japanese", "ja", RegionModel("日本語", R.drawable.ic_language_jp)),
        LanguageModel("Korean", "ko", RegionModel("한국인", R.drawable.ic_language_ko)),
        LanguageModel("Hindi", "hi", RegionModel("हिन्दी", R.drawable.ic_language_hi)),
        LanguageModel("French", "fr", RegionModel("Français", R.drawable.ic_language_fr)),
        LanguageModel("German", "de", RegionModel("Deutsch", R.drawable.ic_language_de)),
    )

    private fun getAllLangWithExtraRegion(): MutableList<LanguageModel> {
        return getAllLang().map {
            it.copy(extraRegion = RegionHelper.getRegionsByLanguage(it.code))
        }.toMutableList()
    }

    private fun getHandPosition(screenOpenCount: Int): Int {
        return if (screenOpenCount <= 1) DEFAULT_POSITION_HAND_ANIMATE else INVALID_POSITION
    }

    private fun getListLangSorted(listLang: MutableList<LanguageModel>): List<LanguageModel> {
        val sortedList = listLang.sortedBy { language ->
            language.mainRegion.isSelected || language.extraRegion.any { it.isSelected }
        }
        return sortedList.reversed().toList()
    }

    private fun getListLangWithHand(
        posHandAnimate: Int,
        listLang: MutableList<LanguageModel>,
    ): List<LanguageModel> {
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