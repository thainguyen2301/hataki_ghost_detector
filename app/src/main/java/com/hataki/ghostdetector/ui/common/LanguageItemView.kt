package com.hataki.ghostdetector.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hataki.ghostdetector.R

class LanguageItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val imgFlag: AppCompatImageView
    private val tvLanguage: AppCompatTextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_language_item, this, true)
        imgFlag = findViewById(R.id.imgFlag)
        tvLanguage = findViewById(R.id.tvLanguage)

        isClickable = true
        isFocusable = true
    }

    fun setLanguage(name: String, flagRes: Int, selected: Boolean = false) {
        tvLanguage.text = name
        imgFlag.setImageResource(flagRes)
        isSelected = selected
    }
}