package com.hataki.ghostdetector.ui.privacy

import com.hataki.ghostdetector.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor() : BaseViewModel() {
    val policyUrl = "https://policies.google.com/privacy?hl=en"
}