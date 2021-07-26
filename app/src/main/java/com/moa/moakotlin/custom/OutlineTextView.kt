package com.moa.moakotlin.custom

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView
import com.moa.moakotlin.R


class OutlineTextView : AppCompatTextView {
    private var hasStroke = false
    private var mStrokeWidth = 0.0f
    private var mStrokeColor = 0

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initView(context, attrs)
    }

    override fun onDraw(canvas: Canvas) {
        if (hasStroke) {
            val states = textColors
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = mStrokeWidth
            setTextColor(mStrokeColor)
            super.onDraw(canvas)
            paint.style = Paint.Style.FILL
            setTextColor(states)
        }
        super.onDraw(canvas)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.OutlineTextView)
        hasStroke = typeArray.getBoolean(R.styleable.OutlineTextView_textStroke, false)
        mStrokeWidth = typeArray.getFloat(R.styleable.OutlineTextView_textStrokeWidth, 0.0f)
        mStrokeColor = typeArray.getColor(R.styleable.OutlineTextView_textStrokeColor, -0x1)
    }
}

