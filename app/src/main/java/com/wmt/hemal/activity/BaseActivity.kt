package com.wmt.hemal.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.wmt.hemal.R
import com.wmt.hemal.extensions.hideKeyboard
import com.wmt.hemal.network.ApiErrorHandle
import com.wmt.hemal.utility.Utils

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(
            this,
            R.style.AppCompatAlertDialogStyle
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId())

        // Initialize
        init()

        // Get Intent Data
        getIntentData()
    }

    override fun onClick(v: View?) {
        hideKeyboard()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()

        //Dismiss Progress Dialog
        dismissProgressDialog()
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)

        //For showing animation while start new activity
        overRidePendingTransitionEnter()
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        super.startActivity(intent, options)

        //For showing animation while start new activity
        overRidePendingTransitionEnter()
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)

        //For showing animation while start new activity
        overRidePendingTransitionEnter()
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)

        //For showing animation while start new activity
        overRidePendingTransitionEnter()
    }

    override fun finish() {
        super.finish()

        //For showing animation while finish activity
        overRidePendingTransitionExit()
    }

    override fun finishAffinity() {
        super.finishAffinity()

        //For showing animation while finish activity
        overRidePendingTransitionExit()
    }

    //region Abstract
    /***
     * To set layout resource to the activity
     */
    @LayoutRes
    abstract fun layoutResourceId(): Int

    /***
     * To initialize basic things for every activity
     */
    abstract fun init()

    /***
     * Get Intent Data from Previous Activity
     */
    abstract fun getIntentData()

    /***
     * Validate form
     */
    abstract fun validate(): Boolean
    //endregion

    /***
     * Overrides the pending Activity transition by performing the "Enter" animation.
     * (From Right to From Left)
     */
    private fun overRidePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    /***
     * Overrides the pending Activity transition by performing the "Exit" animation.
     * (From Left to From Right)
     */
    private fun overRidePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    /***
     * Method to enable back button on activity action bar, this will work if
     * {@code parentActivity} is defined in {@code AndroidManifest.xml}
     */
    fun enableHome() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 5F
    }

    /***
     * Set Navigation Icon Color White
     */
    fun setNavigationIconColor(toolbar: MaterialToolbar) {
        if (toolbar.navigationIcon != null) {
            toolbar.navigationIcon!!.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    R.color.white,
                    BlendModeCompat.SRC_ATOP
                )
        }
    }

    /***
     * Show Progress Dialog
     */
    fun showProgressDialog(msg: String) {
        // Set Progress Dialog Cancelable false
        progressDialog.setCancelable(false)

        if (Utils.isEmptyString(msg)) {
            progressDialog.setMessage(msg);
        } else {
            progressDialog.setMessage(getString(R.string.progress));
        }

        progressDialog.show()
    }

    /***
     * Dismiss Progress Dialog
     */
    fun dismissProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    /***
     * For Showing toast message
     */
    fun showToast(string: String) {
        // Initialize custom view and set message
        val view: View =
            layoutInflater.inflate(R.layout.toast_message, findViewById(R.id.llToastLayout))
        val tvMessage: MaterialTextView = view.findViewById(R.id.tvMessage)
        tvMessage.text = string

        // Initialize toast
        val toast: Toast = Toast(baseContext)

        // Set duration
        toast.duration = Toast.LENGTH_LONG

        // Set view
        toast.view = view

        // Show toast message
        toast.show()
    }

    fun handleApiError(t: Throwable) {
        val errorModel = ApiErrorHandle.traceErrorException(t)

        showToast(errorModel.getErrorMessage())
    }
}