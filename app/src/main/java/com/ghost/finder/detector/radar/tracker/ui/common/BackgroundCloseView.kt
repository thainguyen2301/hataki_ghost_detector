package com.ghost.finder.detector.radar.tracker.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ghost.finder.detector.radar.tracker.R

class BackgroundCloseView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.background_close_view_hataki_1, this, true)
    }
}