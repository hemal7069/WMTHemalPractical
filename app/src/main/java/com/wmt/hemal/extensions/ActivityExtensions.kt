package com.wmt.hemal.extensions

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.wmt.hemal.R

/***
 * Hide Keyboard
 */
fun Activity.hideKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
}

/***
 * Replace Fragment With Animation From Left to Right
 */
fun AppCompatActivity.replaceFragmentWithAnimation(
    container: View,
    fragment: Fragment,
    tag: String
) {
    Handler(Looper.getMainLooper()).post {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.setCustomAnimations(
            R.anim.enter_from_left,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_left
        )

        transaction.replace(container.id, fragment)
        transaction.addToBackStack(tag)
        transaction.commitAllowingStateLoss()
    }
}

/***
 * Replace Fragment With Sliding Up
 */
fun AppCompatActivity.replaceFragmentSlidingUp(container: View, fragment: Fragment, tag: String) {
    Handler(Looper.getMainLooper()).post {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.setCustomAnimations(
            R.anim.enter_from_bottom,
            R.anim.exit_to_bottom,
            R.anim.enter_from_bottom,
            R.anim.exit_to_bottom
        )

        transaction.replace(container.id, fragment)
        transaction.addToBackStack(tag)
        transaction.commitAllowingStateLoss()
    }
}