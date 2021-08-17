package com.moa.moakotlin.custom
import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class CustomViewPager : ViewPager {
var str = "11"
    var specWidth = 0
    var specHeight = 0
    var specWidth2 = 0
    var specHeight2 = 0
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if(specWidth==specWidth2){
            specWidth = widthMeasureSpec
            specHeight = heightMeasureSpec
        }else{
            specHeight2 = heightMeasureSpec
            specWidth2 = widthMeasureSpec
        }
        var heightMeasureSpec = heightMeasureSpec

        val mode = MeasureSpec.getMode(heightMeasureSpec)

        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val h = child.measuredHeight
                if (h > height) height = h
                if(height>2000){
                    height = 2000
                }

            }

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

fun reset(){


}


}