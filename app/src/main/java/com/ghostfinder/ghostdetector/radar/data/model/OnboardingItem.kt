package com.ghostfinder.ghostdetector.radar.data.model

import androidx.annotation.DrawableRes

data class OnboardingItem(
    val title: String,
    @get:DrawableRes
    val image: Int,
)