package com.wmt.hemal.activity

import android.content.Intent
import android.util.Patterns
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.wmt.hemal.R
import com.wmt.hemal.model.Register
import com.wmt.hemal.network.RetrofitClient
import com.wmt.hemal.shared_preference.PreferenceUtils
import com.wmt.hemal.utility.Logger
import com.wmt.hemal.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : BaseActivity() {
    private lateinit var tilUserName: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout

    private lateinit var edtUserName: TextInputEditText
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var edtConfirmPassword: TextInputEditText

    private lateinit var tvRegistration: MaterialTextView

    override fun layoutResourceId(): Int {
        return R.layout.activity_registration
    }

    override fun init() {
        if(PreferenceUtils.isUserLogin(this)){
            goToHomePage()
        }

        //View Initialization
        tilUserName = findViewById(R.id.tilUserName)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)

        edtUserName = findViewById(R.id.edtUserName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)

        tvRegistration = findViewById(R.id.tvRegistration)

        // View Click Events
        tvRegistration.setOnClickListener(this)

        // Remove Error
        Utils.removeError(tilUserName, edtUserName)
        Utils.removeError(tilEmail, edtEmail)
        Utils.removeError(tilPassword, edtPassword)
        Utils.removeError(tilConfirmPassword, edtConfirmPassword)
    }

    override fun onClick(v: View?) {
        super.onClick(v)

        when (v?.id) {
            R.id.tvRegistration -> {
                if (validate()) {
                    apiRegisterUser()
                }
            }
        }
    }

    override fun getIntentData() {

    }

    override fun validate(): Boolean {
        if (!Utils.isEmptyString(edtUserName.text.toString())) {
            tilUserName.error = "Please enter user name. It's required field."
            edtUserName.requestFocus()

            return false
        } else if (!Utils.isEmptyString(edtEmail.text.toString())) {
            tilEmail.error = "Please enter email address. It's required field."
            edtEmail.requestFocus()

            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches()) {
            tilEmail.error = "Please enter valid email address."
            edtEmail.requestFocus()

            return false
        } else if (!Utils.isEmptyString(edtPassword.text.toString())) {
            tilPassword.error = "Please enter password. It's required field."
            edtPassword.requestFocus()

            return false
        } else if (edtPassword.text.toString().length < 8) {
            tilPassword.error = "Password length should be greater or equal to 8 characters."
            edtPassword.requestFocus()

            return false
        } else if (!Utils.isEmptyString(edtConfirmPassword.text.toString())) {
            tilConfirmPassword.error = "Please confirm password."
            edtConfirmPassword.requestFocus()

            return false
        } else if (edtPassword.text.toString() != edtConfirmPassword.text.toString()) {
            tilConfirmPassword.error = "Confirm password does not match."
            edtConfirmPassword.requestFocus()

            return false
        }

        return true
    }

    // Register User
    private fun apiRegisterUser() {
        if (Utils.isOnline(this)) {
            showProgressDialog("")

            RetrofitClient
                .instance
                .registerUser(
                    edtUserName.text.toString(),
                    edtEmail.text.toString(),
                    edtPassword.text.toString()
                )
                .enqueue(object : Callback<Register> {
                    override fun onResponse(call: Call<Register>, response: Response<Register>) {
                        val register: Register? = response.body()

                        Logger.e("Response Code: " + response.code())

                        if (register != null) {
                            if (register.meta.status == "ok") {
                                // Save User data into shared preference
                                PreferenceUtils.setUserLogin(baseContext, true)

                                //User Name
                                PreferenceUtils.setUserName(baseContext, register.data.user.username)

                                // Set User Email Address
                                PreferenceUtils.setUserEmail(baseContext, register.data.user.email)

                                // Set User Profile Pic
                                PreferenceUtils.setProfilePic(baseContext, register.data.user.profilePic)

                                // Set User Id
                                PreferenceUtils.setUserId(baseContext,
                                    register.data.user.id.toString()
                                )

                                // Set token
                                PreferenceUtils.setToken(baseContext, register.data.token.token)

                                // Set Refresh Token
                                PreferenceUtils.setRefreshToken(baseContext, register.data.token.refreshToken)

                                goToHomePage()
                            }

                            showToast(register.meta.message)
                        }

                        dismissProgressDialog()
                    }

                    override fun onFailure(call: Call<Register>, t: Throwable) {
                        dismissProgressDialog()

                        Logger.e(t.toString())

                        handleApiError(t)
                    }

                })
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    /***
     * Redirect user to home screen
     */
    fun goToHomePage() {
        val intent = Intent(baseContext, HomeActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )

        startActivity(intent)
        finish()
    }
}