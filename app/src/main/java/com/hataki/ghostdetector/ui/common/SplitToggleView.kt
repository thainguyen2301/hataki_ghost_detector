package com.hataki.ghostdetector.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hataki.ghostdetector.R

class SplitToggleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val toggleImage: ImageView
    private val toggleTextView: TextView
    private var _isOn = false
    val isOn: Boolean get() = _isOn
    private var listener: ((Boolean) -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.view_split_toggle_hataki_1, this, true)
        toggleImage = view.findViewById(R.id.ivStatus)
        toggleTextView = view.findViewById(R.id.tvStatus)
        updateStatus(_isOn)
        setOnClickListener { toggle() }
    }

    fun setOn(on: Boolean, animate: Boolean = true) {
        if (on == _isOn) return
        _isOn = on
        updateStatus(_isOn)
        listener?.invoke(_isOn)
    }

    fun setOnToggleListener(l: (Boolean) -> Unit) {
        listener = l
    }

    private fun toggle() {
        setOn(!_isOn)
    }

    private fun updateStatus(status: Boolean) {
        if (status) {
            toggleTextView.text = context.getString(R.string.on)
            toggleTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            toggleTextView.text = context.getString(R.string.off)
            toggleTextView.setTextColor(ContextCompat.getColor(context, R.color.color_white_50))
        }
        toggleImage.setImageResource(if (status) R.drawable.ic_btn_on else R.drawable.ic_btn_off)
    }
}