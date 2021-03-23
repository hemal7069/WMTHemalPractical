package com.wmt.hemal.extensions

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.wmt.hemal.R

enum class TransitionAnimation {
    FADE, SLIDE_UP, SLIDE_IN, NO_ANIMATION
}

/***
 * Hide Keyboard in Fragment
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

/***
 * Set Custom Animation
 */
fun FragmentTransaction.setCustomAnimation(type: TransitionAnimation) {
    when (type) {
        TransitionAnimation.SLIDE_IN -> setCustomAnimations(
            R.anim.enter_from_left,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_left
        )

        TransitionAnimation.SLIDE_UP -> setCustomAnimations(
            R.anim.enter_from_bottom,
            R.anim.exit_to_bottom,
            R.anim.enter_from_bottom,
            R.anim.exit_to_bottom
        )

        TransitionAnimation.FADE -> setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.fade_out
        )

        TransitionAnimation.NO_ANIMATION -> return
    }
}

/***
 * New Fragment Instance
 */
inline fun <reified T : Fragment> newFragmentInstance(vararg params: Pair<String, Any>): T =
    T::class.java.newInstance().apply {
        arguments = bundleOf(*params)
    }

/****
 * Show dialog with animation
 */
fun DialogFragment.showWithAnimation(container: View, fragment: Fragment, backstackTag: String) {
    Handler(Looper.getMainLooper()).post {
        val transaction = childFragmentManager.beginTransaction()

        transaction.setCustomAnimations(
            R.anim.enter_from_left,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_left
        )

        transaction.replace(container.id, fragment)
        transaction.addToBackStack(backstackTag)
        transaction.commitAllowingStateLoss()
    }
}