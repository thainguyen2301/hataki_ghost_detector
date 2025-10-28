package com.ghostfinder.ghostdetector.radar.model

import androidx.annotation.Keep

@Keep
data class HiddenAdsVersionConfig(
    val versions: List<String> = emptyList()
)