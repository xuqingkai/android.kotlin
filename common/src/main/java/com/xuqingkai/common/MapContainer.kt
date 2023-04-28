package com.xuqingkai.common

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout

class MapContainer: RelativeLayout {
    private lateinit var mScrollView: ScrollView;
    constructor(context: Context): super(context) { }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {}
    fun setScrollView(scrollView : ScrollView ) {
        this.mScrollView = scrollView;
    }
    //当点击到地图的时候，让ScrollView不拦截事件，把事件传递到子View
    override fun onInterceptTouchEvent(motionEvent : MotionEvent) : Boolean {
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            mScrollView.requestDisallowInterceptTouchEvent(false);
        } else {
            mScrollView.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }

    override fun onTouchEvent(motionEvent : MotionEvent): Boolean {
        return true;
    }
}