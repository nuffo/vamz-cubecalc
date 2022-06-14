package com.example.cubecalc.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.TextView


class VerticalTextView : androidx.appcompat.widget.AppCompatTextView {
    private var _width : Float = 0.0f
    private var _height: Float = 0.0f
    private val _bounds: Rect = Rect()

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) :
            super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context) : super(context) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // vise versa
        _height = measuredWidth.toFloat()
        _width = measuredHeight.toFloat()
        setMeasuredDimension(_width.toInt(), _height.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()

        canvas.translate(_width, _height)
        canvas.rotate(-90f)

        val paint = paint
        paint.color = textColors.defaultColor

        val text = text()

        paint.getTextBounds(text, 0, text.length, _bounds)
        canvas.drawText(text, compoundPaddingLeft.toFloat(), (_bounds.height() - _width) / 2, paint)

        canvas.restore()
    }

    private fun text(): String {
        return super.getText().toString()
    }
}