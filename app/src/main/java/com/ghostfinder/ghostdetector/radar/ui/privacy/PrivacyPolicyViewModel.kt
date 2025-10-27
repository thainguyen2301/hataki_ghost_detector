package com.ghostfinder.ghostdetector.radar.ui.privacy

import com.ghostfinder.ghostdetector.radar.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor() : BaseViewModel() {
    val policyUrl = "https://sites.google.com/view/ghostdetector-radarfinder"
}