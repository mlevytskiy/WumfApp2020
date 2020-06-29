package com.core.wumfapp2020.viewmodel.home


import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

class TagBadgeSpannable(private val textColor: Int, private val backgroundColor: Int) : ReplacementSpan() {

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val oneLetterSize = measureText(paint, " ", 0, 1)
        val cornerRadius = oneLetterSize * 2.5
        val rect = RectF(x, top.toFloat(), x + measureText(paint, text, start, end), bottom.toFloat())
        paint.color = backgroundColor
        canvas.drawRoundRect(rect, cornerRadius.toFloat(), cornerRadius.toFloat(), paint)
        paint.color = textColor

        canvas.drawText(text, start, end, x, y.toFloat(), paint)
    }

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return Math.round(paint.measureText(text, start, end))
    }

    private fun measureText(paint: Paint, text: CharSequence, start: Int, end: Int): Float {
        return paint.measureText(text, start, end)
    }
}
