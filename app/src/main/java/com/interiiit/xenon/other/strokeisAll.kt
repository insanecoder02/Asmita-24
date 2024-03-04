package com.interiiit.xenon.other


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class StrokeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var strokeWidth = 2
    private var strokeColor = 0xFF000000.toInt() // black by default

    override fun onDraw(canvas: Canvas) {
        // Draw the text with stroke
        paint.strokeWidth = strokeWidth.toFloat()
        paint.style = Paint.Style.STROKE
        paint.color = strokeColor
        super.onDraw(canvas)
    }

    fun setStrokeWidth(width: Int) {
        strokeWidth = width
        invalidate()
    }

    fun setStrokeColor(color: Int) {
        strokeColor = color
        invalidate()
    }
}
