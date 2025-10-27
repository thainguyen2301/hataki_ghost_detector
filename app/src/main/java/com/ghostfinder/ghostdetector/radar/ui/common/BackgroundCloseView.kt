package com.ghostfinder.ghostdetector.radar.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ghostfinder.ghostdetector.radar.R

class BackgroundCloseView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.background_close_view, this, true)
    }
}