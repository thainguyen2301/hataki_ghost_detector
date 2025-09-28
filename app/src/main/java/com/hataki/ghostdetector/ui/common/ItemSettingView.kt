package com.hataki.ghostdetector.ui.common


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.hataki.ghostdetector.R

class ItemSettingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val ivIcon: ImageView
    private val tvTitle: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.item_setting, this, true)
        ivIcon = findViewById(R.id.iv_icon)
        tvTitle = findViewById(R.id.tv_title)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ItemSettingView,
            0, 0
        ).apply {
            try {
                tvTitle.text = getString(R.styleable.ItemSettingView_titleText) ?: ""
                ivIcon.setImageResource(
                    getResourceId(
                        R.styleable.ItemSettingView_iconSrc,
                        R.drawable.ic_launcher_foreground
                    )
                )
            } finally {
                recycle()
            }
        }
    }

    fun setTitle(text: String) {
        tvTitle.text = text
    }

    fun setIcon(@DrawableRes resId: Int) {
        ivIcon.setImageResource(resId)
    }
}
