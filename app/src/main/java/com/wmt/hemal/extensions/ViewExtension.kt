package com.wmt.hemal.extensions

import android.view.View

const val CLICK_THROTTLE_DELAY = 200L

fun View.onThrottledClick(
    throttleDelay: Long = CLICK_THROTTLE_DELAY,
    onClick: (View) -> Unit
) {
    setOnClickListener {
        onClick(this)

        isClickable = false

        postDelayed({isClickable = true}, throttleDelay)
    }
}