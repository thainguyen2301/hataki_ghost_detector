package com.hataki.ghostdetector.ui.common


import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hataki.ghostdetector.R

class SplitToggleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val root: ConstraintLayout
    private val leftContainer: FrameLayout
    private val rightContainer: FrameLayout

    private val edgeButtonLeft: View
    private val edgeButtonRight: View
    private val leftIcon: ImageView
    private val rightIcon: ImageView

    private val leftLightColor = Color.parseColor("#049F04")
    private val brightColor = Color.parseColor("#1EEA3F")
    private val darkColor = Color.parseColor("#0B6B2A")

    private var leftBg: GradientDrawable = makeBackground(darkColor)
    private var rightBg: GradientDrawable = makeBackground(darkColor)

    private var _isOn = false
    val isOn: Boolean get() = _isOn

    private var listener: ((Boolean) -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_split_toggle, this, true)

        root = findViewById(R.id.ll_root)
        leftContainer = findViewById(R.id.btn_off)
        rightContainer = findViewById(R.id.btn_on)
        leftIcon = findViewById(R.id.iv_left)
        rightIcon = findViewById(R.id.iv_right)
        edgeButtonLeft = findViewById(R.id.edge_button_off)
        edgeButtonRight = findViewById(R.id.edge_button_on)

        leftBg = makeBackground(darkColor)
        rightBg = makeBackground(darkColor)

        leftContainer.background = leftBg
        rightContainer.background = rightBg
        setStateInternal(false, animate = false)
        setOnClickListener { toggle() }
        leftContainer.setOnClickListener { if (_isOn) setOn(false) else setOn(false) }
        rightContainer.setOnClickListener { if (!_isOn) setOn(true) else setOn(true) }
    }

    fun setOn(on: Boolean, animate: Boolean = true) {
        if (on == _isOn) return
        setStateInternal(on, animate)
        listener?.invoke(_isOn)
    }

    fun setOnToggleListener(l: (Boolean) -> Unit) {
        listener = l
    }

    private fun toggle() {
        setOn(!_isOn)
    }

    private fun setStateInternal(on: Boolean, animate: Boolean) {
        _isOn = on
        val leftTarget = if (_isOn) darkColor else leftLightColor
        val rightTarget = if (_isOn) brightColor else darkColor
        resizeButtons()
        if (animate) {
            animateColor(leftBg, (leftBg.color?.defaultColor ?: darkColor), leftTarget)
            animateColor(rightBg, (rightBg.color?.defaultColor ?: darkColor), rightTarget)
        } else {
            leftBg.setColor(leftTarget)
            rightBg.setColor(rightTarget)
        }
    }

    private fun resizeButtons() {
        val delta = dpToPx(8f)

        val btnOffParams = leftContainer.layoutParams
        val btnOnParams = rightContainer.layoutParams

        if (_isOn) {
            btnOffParams.width = dpToPxInt(32f)
            btnOnParams.width = dpToPxInt(40f)
            edgeButtonLeft.visibility = VISIBLE
            edgeButtonRight.visibility = GONE
        } else {
            btnOffParams.width = dpToPxInt(40f)
            btnOnParams.width = dpToPxInt(32f)
            edgeButtonLeft.visibility = GONE
            edgeButtonRight.visibility = VISIBLE
        }

        leftContainer.layoutParams = btnOffParams
        rightContainer.layoutParams = btnOnParams
    }

    private fun makeBackground(fillColor: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(fillColor)
        }
    }

    private fun animateColor(drawable: GradientDrawable, from: Int, to: Int) {
        val anim = ValueAnimator.ofObject(ArgbEvaluator(), from, to)
        anim.duration = 180
        anim.addUpdateListener { valueAnimator ->
            val color = valueAnimator.animatedValue as Int
            drawable.setColor(color)
        }
        anim.start()
    }

    private fun dpToPx(dp: Float): Float =
        dp * resources.displayMetrics.density

    private fun dpToPxInt(dp: Float): Int =
        (dpToPx(dp) + 0.5f).toInt()
}
