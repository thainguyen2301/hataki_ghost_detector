package com.hataki.ghostdetector.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withRotation
import com.hataki.ghostdetector.data.model.Target
import kotlin.math.min

class RadarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val outerBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 2f * resources.displayMetrics.density
    }

    private val innerLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 1f * resources.displayMetrics.density
        alpha = 100
    }

    private val sweepPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        alpha = 120
        style = Paint.Style.FILL
    }

    private var gradientPaint: Paint? = null
    private var radius = 0f
    private var sweepAngle = 0f
    private var azimuth = 0f

    private val targetPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val targets = mutableListOf<Target>()

    private var blinkPhase = 0f

    fun addTarget(angle: Float, distance: Float) {
        targets.add(Target(angle, distance.coerceIn(0f, 1f)))
    }

    private val sweepRunnable = object : Runnable {
        override fun run() {
            sweepAngle = (sweepAngle + 3f) % 360f
            targets.forEach { target ->
                if (target.alphaIncreasing) {
                    target.alpha += 15
                    if (target.alpha >= 255) {
                        target.alpha = 255
                        target.alphaIncreasing = false
                    }
                } else {
                    target.alpha -= 15
                    if (target.alpha <= 50) {
                        target.alpha = 50
                        target.alphaIncreasing = true
                    }
                }
            }
            invalidate()
            postDelayed(this, 30)
        }
    }

    fun startSweep() {
        removeCallbacks(sweepRunnable)
        post(sweepRunnable)
    }

    fun stopSweep() {
        removeCallbacks(sweepRunnable)
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            blinkPhase += 0.1f
            val blink = (Math.sin(blinkPhase.toDouble()) * 0.5 + 0.5) * 255
            val alphaValue = blink.toInt().coerceIn(50, 255)

            targets.forEach {
                it.distance -= 0.002f
                if (it.distance < 0f) it.distance = 1f
                it.alpha = alphaValue
            }

            invalidate()
            postDelayed(this, 16)
        }
    }

    fun setAzimuth(angle: Float) {
        azimuth = angle
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f
        radius = min(cx, cy) * 0.9f

        if (gradientPaint == null) {
            gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = RadialGradient(
                    cx, cy, radius,
                    intArrayOf(Color.parseColor("#02B802"), Color.parseColor("#011401")),
                    floatArrayOf(0f, 1f),
                    Shader.TileMode.CLAMP
                )
            }
        }
        canvas.drawCircle(cx, cy, radius, gradientPaint!!)

        canvas.withRotation(-azimuth, cx, cy) {
            drawRadarGrid(this, cx, cy)
        }
        drawSweep(canvas, cx, cy)

        drawTargets(canvas, cx, cy)
    }

    private fun drawTargets(canvas: Canvas, cx: Float, cy: Float) {
        val targetRadius = 22f * resources.displayMetrics.density / 2f

        targets.forEach { target ->
            val angleRad = Math.toRadians(target.angle.toDouble())
            val r = radius * target.distance
            val x = (cx + r * Math.cos(angleRad)).toFloat()
            val y = (cy + r * Math.sin(angleRad)).toFloat()

            val gradient = RadialGradient(
                x, y, targetRadius,
                intArrayOf(Color.RED, Color.TRANSPARENT),
                floatArrayOf(0f, 1f),
                Shader.TileMode.CLAMP
            )

            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = gradient
                alpha = target.alpha
            }

            canvas.drawCircle(x, y, targetRadius, paint)
        }
    }

    private fun drawRadarGrid(canvas: Canvas, cx: Float, cy: Float) {
        canvas.drawCircle(cx, cy, radius, outerBorderPaint)

        for (i in 1..3) {
            canvas.drawCircle(cx, cy, radius * i / 4, innerLinePaint)
        }

        for (i in 0 until 360 step 30) {
            val rad = Math.toRadians(i.toDouble())
            val x = (cx + radius * Math.cos(rad)).toFloat()
            val y = (cy + radius * Math.sin(rad)).toFloat()
            canvas.drawLine(cx, cy, x, y, innerLinePaint)
        }
    }

    private fun drawSweep(canvas: Canvas, cx: Float, cy: Float) {
        val sweep = 45f
        val startAngle = sweepAngle - sweep / 2f

        val rect = RectF(cx - radius, cy - radius, cx + radius, cy + radius)
        val gradient = SweepGradient(
            cx, cy,
            intArrayOf(
                Color.TRANSPARENT,
                Color.parseColor("#AA00FF00"),
                Color.parseColor("#FF00FF00"),
                Color.parseColor("#AA00FF00"),
                Color.TRANSPARENT
            ),
            floatArrayOf(
                0f,
                0.45f,
                0.5f,
                0.55f,
                1f
            )
        )

        val sweepPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            shader = gradient
        }

        canvas.save()
        canvas.rotate(270f + startAngle, cx, cy)
        canvas.drawArc(rect, 0f, sweep, true, sweepPaint)
        canvas.restore()
    }
}

