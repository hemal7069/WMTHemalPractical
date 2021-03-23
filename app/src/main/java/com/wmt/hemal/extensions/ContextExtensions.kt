package com.wmt.hemal.extensions

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

/***
 * Hide Keyboard
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/***
 * Get Color From Color Resource
 */
@ColorInt
fun Context.color(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}

/**
 * Get color from resources with applied [opacity]
 */
@ColorInt
fun Context.colorWithOpacity(@ColorRes res: Int, @IntRange(from = 0, to = 100) opacity: Int): Int {
    return color(res).withOpacity(opacity)
}

fun Context.colors(@ColorRes stateListRes: Int): ColorStateList? {
    return ContextCompat.getColorStateList(this, stateListRes)
}

/***
 * Get String from String Resources
 */
fun Context.string(@StringRes res: Int): String {
    return getString(res)
}

/***
 * Get Drawable From Drawable Resource
 */
fun Context.drawable(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

/***
 * Tinted Drawable using color resource and drawable resource
 */
fun Context.tintedDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable? {
    val tint: Int = color(colorId)

    val drawable = drawable(drawableId)
    drawable?.mutate()

    drawable?.let {
        it.mutate()
        DrawableCompat.setTint(it, tint)
    }

    return drawable
}

/**
 * Get status bar height
 * @param restrictToLollipop indicator if status bar height resource should be lookup only on versions higher than Lollipop
 */
fun Context.statusBarHeight(restrictToLollipop: Boolean = true): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

    if (resourceId > 0 && (!restrictToLollipop || (restrictToLollipop && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP))) {
        result = resources.getDimensionPixelSize(resourceId)
    }

    return result
}