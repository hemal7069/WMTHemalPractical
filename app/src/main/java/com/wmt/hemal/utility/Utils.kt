package com.wmt.hemal.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wmt.hemal.extensions.getConnectivityManager

class Utils {
    companion object {
        /***
         * For Checking for String is empty or not
         */
        fun isEmptyString(value: String): Boolean {
            return value.isNotEmpty() && value.trim() != ""
        }

        /***
         * Check user is online or not before API call
         */
        fun isOnline(context: Context): Boolean {
            var isConnected = false
            val connectivityManager: ConnectivityManager = context.getConnectivityManager()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

                isConnected = when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

                    else -> false
                }
            } else {
                connectivityManager.run {
                    activeNetworkInfo?.run {
                        isConnected = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true

                            ConnectivityManager.TYPE_MOBILE -> true

                            ConnectivityManager.TYPE_ETHERNET -> true

                            else -> false
                        }
                    }
                }
            }

            return isConnected
        }

        /***
         * Check is email address valid or not
         */
        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        /****
         * Function to remove error from edit text
         */
        fun removeError(inputLayout: TextInputLayout, editText: TextInputEditText) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (inputLayout.error != null) {
                        inputLayout.error = null
                    }

                    if (editText.error != null) {
                        editText.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }
}