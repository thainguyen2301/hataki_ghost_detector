package com.hataki.ghostdetector.data.model

import android.graphics.Bitmap

data class GhostSprite(
    val id: Long,
    var angleDeg: Float,
    var distance: Float,
    val bitmap: Bitmap,
    var screenX: Float = 0f,
    var screenY: Float = 0f,
    var visible: Boolean = true,
)