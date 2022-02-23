package com.example.forecast.feature_forecast.presentation

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import kotlin.math.abs

class SwipeListener(
    private val leftSwipeNavigation: LeftSwipeNavigation? = null,
    private val rightSwipeNavigation: RightSwipeNavigation? = null,
) : GestureDetector.SimpleOnGestureListener() {
    private val threshold = 100

    override fun onDown(event: MotionEvent): Boolean {
        return true
    }

    override fun onFling(
        event1: MotionEvent?,
        event2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        var result = false

        if (event1 == null || event2 == null) return result

        val diffY: Float = event1.y - event1.y
        val diffX: Float = event2.x - event1.x
        if (abs(diffX) > abs(diffY) && abs(diffX) > threshold && abs(velocityX) > threshold) {
            if (diffX < 0) {
                leftSwipeNavigation?.onLeftSwipe()
            } else {
                rightSwipeNavigation?.onRightSwipe()
            }
            result = true

        }

        return result
    }
}

interface LeftSwipeNavigation {
    fun onLeftSwipe()
}

interface RightSwipeNavigation {
    fun onRightSwipe()
}

