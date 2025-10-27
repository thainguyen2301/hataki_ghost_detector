package com.ghostfinder.ghostdetector.radar.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.ghostfinder.ghostdetector.radar.R

class ToggleSwitchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val bgView: View
    private val circle: View
    private var isOn = false

    init {
        bgView = View(context).apply {
            background = ContextCompat.getDrawable(context, R.drawable.ic_switch_off)
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        addView(bgView)

        circle = View(context).apply {
            setBackgroundResource(R.drawable.circle_shape)
            val size = 18
            layoutParams = LayoutParams(size, size).apply {
                gravity = Gravity.START or Gravity.CENTER_VERTICAL
                marginStart = 8
            }
        }
        addView(circle)

        setOnClickListener { toggle() }
    }

    private fun toggle() {
        isOn = !isOn
        bgView.background = ContextCompat.getDrawable(
            context,
            if (isOn) R.drawable.ic_switch_on else R.drawable.ic_switch_off
        )

        val targetX = if (isOn) width - circle.width - 8 else 8
        circle.animate().x(targetX.toFloat()).setDuration(200).start()
    }

    fun setOn(on: Boolean) {
        if (on != isOn) toggle()
    }

    fun isOn() = isOn
}
