package com.wmt.hemal.shared_preference

import android.content.Context
import com.wmt.hemal.R
import com.wmt.hemal.utility.Constants

class PreferenceUtils {
    companion object {
        /***
         * Get Shared Preference
         */
        private fun getSharedPreference(context: Context): ObscureSharePreference {
            /*return context.applicationContext.getSharedPreferences(
                context.getString(R.string.app_name),
                Context.MODE_PRIVATE
            )*/

            return ObscureSharePreference.getPrefs(
                context,
                context.getString(R.string.app_name),
                Context.MODE_PRIVATE
            )!!
        }

        //region Getter Methods
        fun isUserLogin(context: Context): Boolean {
            return getSharedPreference(context).getBoolean(Constants.IS_USER_LOGIN, false)
        }

        fun getUserId(context: Context): String {
            return getSharedPreference(context).getString(Constants.KEY_USER_ID, "")!!
        }

        fun getUserName(context: Context): String {
            return getSharedPreference(context).getString(Constants.KEY_USER_NAME, "")!!
        }

        fun getUserEmail(context: Context): String {
            return getSharedPreference(context).getString(Constants.KEY_EMAIL, "")!!
        }

        fun getProfilePic(context: Context): String {
            return getSharedPreference(context).getString(Constants.KEY_PROFILE_PIC, "")!!
        }

        fun getToken(context: Context): String {
            return getSharedPreference(context).getString(Constants.KEY_TOKEN, "")!!
        }

        fun getRefreshToken(context: Context): String {
            return getSharedPreference(context).getString(Constants.KEY_REFRESH_TOKEN, "")!!
        }
        //endregion

        //region Setter Methods
        fun setUserLogin(context: Context, isUserLogin: Boolean) {
            getSharedPreference(context).edit().putBoolean(Constants.IS_USER_LOGIN, isUserLogin)
                .apply()
        }

        fun setUserId(context: Context, userId: String) {
            getSharedPreference(context).edit().putString(Constants.KEY_USER_ID, userId).apply()
        }

        fun setUserName(context: Context, userName: String) {
            getSharedPreference(context).edit().putString(Constants.KEY_USER_NAME, userName).apply()
        }

        fun setUserEmail(context: Context, userName: String) {
            getSharedPreference(context).edit().putString(Constants.KEY_EMAIL, userName).apply()
        }

        fun setProfilePic(context: Context, userName: String) {
            getSharedPreference(context).edit().putString(Constants.KEY_PROFILE_PIC, userName).apply()
        }

        fun setToken(context: Context, deviceToken: String) {
            getSharedPreference(context).edit().putString(Constants.KEY_TOKEN, deviceToken)
                .apply()
        }

        fun setRefreshToken(context: Context, deviceToken: String) {
            getSharedPreference(context).edit().putString(Constants.KEY_REFRESH_TOKEN, deviceToken)
                .apply()
        }
        //endregion

        /***
         * Clear shared preference
         */
        fun clearData(context: Context) {
            setUserLogin(context, false)

            getSharedPreference(context).edit().clear().apply()
        }
    }
}