package com.hataki.ghostdetector.data.model

data class Target(
    var angle: Float,
    var distance: Float,
    var alpha: Int = 255,
    var alphaIncreasing: Boolean = false
)