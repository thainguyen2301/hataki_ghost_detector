package com.hataki.ghostdetector.ui.onboard

import androidx.lifecycle.viewModelScope
import com.hataki.ghostdetector.data.model.common.UIState
import com.hataki.ghostdetector.data.repository.onboard.OnboardRepository
import com.hataki.ghostdetector.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardRepository: OnboardRepository
) : BaseViewModel() {
    private var _saveIsOnBoardingState = MutableStateFlow<UIState<Boolean>>(UIState.Idle)
    val saveIsOnBoardingState: StateFlow<UIState<Boolean>> = _saveIsOnBoardingState

    fun saveIsOnBoarding() {
        viewModelScope.launch {
            onboardRepository.saveIsOnBoarding(true)
                .onSuccess { isSuccess ->
                    _saveIsOnBoardingState.value = UIState.Success(isSuccess)
                }
        }
    }
}