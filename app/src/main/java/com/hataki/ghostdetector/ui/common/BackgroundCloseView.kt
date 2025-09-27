package com.hataki.ghostdetector.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.hataki.ghostdetector.R

class BackgroundCloseView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.background_close_view, this, true)
    }
}