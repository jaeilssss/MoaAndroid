package com.moa.moakotlin.custom

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import android.view.View
import androidx.annotation.Nullable


class DynamicHeightViewPager : ViewPager {
    var mCurrentView: View? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {}

    //    @Override
    //    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //
    //        int height = 0;
    //        int childWidthSpec = MeasureSpec.makeMeasureSpec(
    //                Math.max(0, MeasureSpec.getSize(widthMeasureSpec) -
    //                        getPaddingLeft() - getPaddingRight()),
    //                MeasureSpec.getMode(widthMeasureSpec)
    //        );
    //        for (int i = 0; i < getChildCount(); i++) {
    //            View child = getChildAt(i);
    //            child.measure(childWidthSpec, MeasureSpec.UNSPECIFIED);
    //            int h = child.getMeasuredHeight();
    //            if (h > height) height = h;
    //        }
    //
    //        if (height != 0) {
    //            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
    //        }
    //
    //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //    }
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mCurrentView != null) {
            mCurrentView!!.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val height = Math.max(0, mCurrentView!!.getMeasuredHeight())
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun measureCurrentView(currentView: View?) {
        mCurrentView = currentView
        requestLayout()
    }
}