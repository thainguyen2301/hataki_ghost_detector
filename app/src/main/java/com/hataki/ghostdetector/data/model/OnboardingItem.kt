package com.hataki.ghostdetector.data.model

import androidx.annotation.DrawableRes

data class OnboardingItem(
    val title: String,
    @get:DrawableRes
    val image: Int,
)