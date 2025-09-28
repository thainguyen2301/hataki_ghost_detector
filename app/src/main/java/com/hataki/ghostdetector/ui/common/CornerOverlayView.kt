package com.hataki.ghostdetector.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CornerOverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 4f * resources.displayMetrics.density // 4px ≈ 4dp
        style = Paint.Style.STROKE
    }

    private val cornerLength = 32f * resources.displayMetrics.density // chiều dài đoạn góc
    private val radius = 12f * resources.displayMetrics.density       // bo tròn
    private val inset = 16f * resources.displayMetrics.density        // cách mép màn hình

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        canvas.drawArc(
            inset, inset,
            inset + 2 * radius, inset + 2 * radius,
            180f, 90f, false, paint
        )
        canvas.drawLine(inset + radius, inset, inset + cornerLength, inset, paint)
        canvas.drawLine(inset, inset + radius, inset, inset + cornerLength, paint)

        canvas.drawArc(
            w - inset - 2 * radius, inset,
            w - inset, inset + 2 * radius,
            270f, 90f, false, paint
        )
        canvas.drawLine(w - inset - radius, inset, w - inset - cornerLength, inset, paint)
        canvas.drawLine(w - inset, inset + radius, w - inset, inset + cornerLength, paint)

        // bottom-left
        canvas.drawArc(
            inset, h - inset - 2 * radius,
            inset + 2 * radius, h - inset,
            90f, 90f, false, paint
        )
        canvas.drawLine(inset, h - inset - radius, inset, h - inset - cornerLength, paint)
        canvas.drawLine(inset + radius, h - inset, inset + cornerLength, h - inset, paint)

        // bottom-right
        canvas.drawArc(
            w - inset - 2 * radius, h - inset - 2 * radius,
            w - inset, h - inset,
            0f, 90f, false, paint
        )
        canvas.drawLine(w - inset - radius, h - inset, w - inset - cornerLength, h - inset, paint)
        canvas.drawLine(w - inset, h - inset - radius, w - inset, h - inset - cornerLength, paint)
    }
}

