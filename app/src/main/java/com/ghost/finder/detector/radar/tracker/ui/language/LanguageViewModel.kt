package com.ghost.finder.detector.radar.tracker.ui.language

import androidx.lifecycle.viewModelScope
import com.ghost.finder.detector.radar.tracker.data.model.common.UIState
import com.ghost.finder.detector.radar.tracker.data.repository.language.LanguageRepository
import com.ghost.finder.detector.radar.tracker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageRepository: LanguageRepository
) : BaseViewModel() {
    private var _currentLanguageState = MutableStateFlow<UIState<String?>>(UIState.Idle)
    val currentLanguageState: StateFlow<UIState<String?>> = _currentLanguageState
    var selectedLanguage: String? = null

    fun saveLanguage() {
        viewModelScope.launch {
            languageRepository.changeLanguage(selectedLanguage)
        }
    }

    fun getCurrentLanguage() {
        viewModelScope.launch {
            languageRepository.getCurrentLanguage().onSuccess { lang ->
                _currentLanguageState.value = UIState.Success(lang)
            }
        }
    }
}