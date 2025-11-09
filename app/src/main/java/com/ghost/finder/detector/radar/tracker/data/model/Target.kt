package com.ghost.finder.detector.radar.tracker.data.model

data class Target(
    var angle: Float,
    var distance: Float,
    var alpha: Int = 255,
    var alphaIncreasing: Boolean = false
)