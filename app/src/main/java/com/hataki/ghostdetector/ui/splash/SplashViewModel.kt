package com.hataki.ghostdetector.ui.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hataki.ghostdetector.data.repository.onboard.OnboardRepository
import com.hataki.ghostdetector.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onboardRepository: OnboardRepository
) : BaseViewModel() {
    var isOnBoardingState: Boolean = false

    fun getISOnboardingState() {
        viewModelScope.launch {
            onboardRepository.getIsOnBoarding()
                .collect { data ->
                    isOnBoardingState = data ?: false
                }

        }
    }
}