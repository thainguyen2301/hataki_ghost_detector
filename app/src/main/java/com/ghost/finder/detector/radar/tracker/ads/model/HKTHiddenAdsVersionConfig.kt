package com.ghost.finder.detector.radar.tracker.ads.model

import androidx.annotation.Keep

@Keep
data class HiddenAdsVersionConfig(
    val versions: List<String> = emptyList()
)