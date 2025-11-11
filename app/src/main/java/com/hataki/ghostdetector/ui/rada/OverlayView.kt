package com.hataki.ghostdetector.ui.rada

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.hataki.ghostdetector.data.model.GhostSprite
import kotlin.math.max
import kotlin.math.min


class OverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ghosts = mutableListOf<GhostSprite>()

    var deviceAzimuthDeg: Float = 0f
        set(value) {
            field = (value % 360 + 360) % 360
            invalidate()
        }
    private val maxGhostCount = 4
    private var cx = 0f
    private var cy = 0f
    private var maxRadius = 0f

    fun setGhosts(list: List<GhostSprite>) {
        synchronized(ghosts) {
            ghosts.clear()
            ghosts.addAll(list)
        }
        invalidate()
    }

    fun addGhost(g: GhostSprite) {
        synchronized(ghosts) {
            if (ghosts.size >= maxGhostCount) {

                ghosts.removeAt(0)
            }
            ghosts.add(g)
        }
        invalidate()
    }

    fun removeGhostById(id: Long) {
        synchronized(ghosts) {
            ghosts.removeAll { it.id == id }
        }
        invalidate()
    }

    fun clearGhosts() {
        synchronized(ghosts) {
            ghosts.clear()
        }
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cx = w / 2f
        cy = h / 2f
        maxRadius = max(100f, min(w.toFloat(), h.toFloat()) / 2f - 100f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        synchronized(ghosts) {
            for (g in ghosts) {

                val deltaDeg = normalizeAngle(g.angleDeg - deviceAzimuthDeg)
                val rad = Math.toRadians(deltaDeg.toDouble()).toFloat()

                val r = g.distance.coerceIn(0f, 1f) * maxRadius

                val x = cx + r * kotlin.math.sin(rad)
                val y = cy + r * kotlin.math.cos(rad) * 0.6f + (1f - g.distance) * 120f

                g.screenX = x
                g.screenY = y

                val bmp = g.bitmap
                val scale = 0.6f + (1f - g.distance) * 0.8f

                val drawW = bmp.width * scale
                val drawH = bmp.height * scale

                val left = x - drawW / 2f
                val top = y - drawH / 2f

                canvas.save()
                canvas.translate(left, top)
                canvas.scale(scale, scale)
                canvas.drawBitmap(bmp, 0f, 0f, paint)
                canvas.restore()
            }
        }
    }

    private fun normalizeAngle(a: Float): Float {
        var x = a % 360f
        if (x > 180f) x -= 360f
        if (x < -180f) x += 360f
        return x
    }
}