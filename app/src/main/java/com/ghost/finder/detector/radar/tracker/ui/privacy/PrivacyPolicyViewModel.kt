package com.ghost.finder.detector.radar.tracker.ui.privacy

import com.ghost.finder.detector.radar.tracker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor() : BaseViewModel() {
    val policyUrl = "https://policies.google.com/privacy?hl=en"
}