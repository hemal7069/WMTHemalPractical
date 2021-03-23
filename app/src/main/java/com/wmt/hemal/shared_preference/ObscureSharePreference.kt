package com.wmt.hemal.shared_preference

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.provider.Settings
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec

class ObscureSharePreference(
    private val context: Context,
    private var sharedPreferences: SharedPreferences
) : SharedPreferences {
    /***
     * SharedPreference Editor Class
     * @return      -       SharedPreference Editor Class
     */
    override fun edit(): Editor {
        return Editor()
    }

    override fun getAll(): Map<String, *> {
        throw UnsupportedOperationException()
    }

    override fun getBoolean(key: String, value: Boolean): Boolean {
        return try {
            if (sharedPreferences.getString(key, null) != null)
                java.lang.Boolean.parseBoolean(
                    decrypt(sharedPreferences.getString(key, null))
                )
            else
                value
        } catch (e: Exception) {
            false
        }
    }

    override fun getFloat(key: String, value: Float): Float {
        return try {
            if (sharedPreferences.getString(key, null) != null)
                decrypt(sharedPreferences.getString(key, null)).toFloat()
            else
                value
        } catch (e: Exception) {
            0.0f
        }
    }

    override fun getInt(key: String, value: Int): Int {
        return try {
            if (sharedPreferences.getString(key, null) != null) decrypt(
                sharedPreferences.getString(
                    key,
                    null
                )
            ).toInt() else value
        } catch (e: Exception) {
            0
        }
    }

    override fun getLong(key: String, value: Long): Long {
        return try {
            if (sharedPreferences.getString(key, null) != null) decrypt(
                sharedPreferences.getString(
                    key,
                    null
                )
            ).toLong() else value
        } catch (e: Exception) {
            0
        }
    }

    override fun getString(key: String, value: String?): String? {
        return try {
            if (sharedPreferences.getString(key, null) != null) decrypt(
                sharedPreferences.getString(
                    key,
                    null
                )
            ) else value
        } catch (e: Exception) {
            ""
        }
    }

    override fun getStringSet(key: String, set: Set<String>?): Set<String>? {
        throw RuntimeException("This class does not work with String Sets.")
    }

    override fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun registerOnSharedPreferenceChangeListener(ospcl: OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(ospcl)
    }

    override fun unregisterOnSharedPreferenceChangeListener(ospcl: OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(ospcl)
    }

    /***
     * Shared Preference Editor Class
     */
    inner class Editor : SharedPreferences.Editor {
        /***
         * Shared Preference Editor Reference
         */
        private val editor: SharedPreferences.Editor = sharedPreferences.edit()

        //endregion
        override fun putBoolean(key: String, value: Boolean): Editor {
            editor.putString(key, encrypt(java.lang.Boolean.toString(value)))
            return this
        }

        override fun putFloat(key: String, value: Float): Editor {
            editor.putString(key, encrypt(value.toString()))
            return this
        }

        override fun putInt(key: String, value: Int): Editor {
            editor.putString(key, encrypt(value.toString()))
            return this
        }

        override fun putLong(key: String, value: Long): Editor {
            editor.putString(key, encrypt(value.toString()))
            return this
        }

        override fun putString(key: String, value: String?): Editor {
            editor.putString(key, encrypt(value))
            return this
        }

        override fun putStringSet(key: String, value: Set<String>?): Editor {
            throw RuntimeException("This class does not work with String Sets.")
        }

        override fun remove(key: String): Editor {
            editor.remove(key)
            return this
        }

        override fun clear(): Editor {
            editor.clear()
            return this
        }

        override fun commit(): Boolean {
            return editor.commit()
        }

        override fun apply() {
            editor.apply()
        }

    }

    /***
     * Encrypt string.
     * @param value the value
     * @return the string
     */
    private fun encrypt(value: String?): String {
        return try {
            val bytes = value?.toByteArray(charset(UTF8)) ?: ByteArray(0)
            val keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
            val key = keyFactory.generateSecret(PBEKeySpec(SECRIT))

            val pbeCipher = Cipher.getInstance("PBEWithMD5AndDES")
            pbeCipher.init(
                Cipher.ENCRYPT_MODE,
                key,
                PBEParameterSpec(
                    Settings.Secure.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    ).toByteArray(charset(UTF8)), 20
                )
            )

            String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP), Charsets.UTF_8)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /***
     * Decrypt string.
     * @param value the value
     * @return the string
     */
    private fun decrypt(value: String?): String {
        return try {
            val bytes = if (value != null) Base64.decode(value, Base64.DEFAULT) else ByteArray(0)
            val keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
            val key = keyFactory.generateSecret(PBEKeySpec(SECRIT))

            val pbeCipher = Cipher.getInstance("PBEWithMD5AndDES")
            pbeCipher.init(
                Cipher.DECRYPT_MODE,
                key,
                PBEParameterSpec(
                    Settings.Secure.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    ).toByteArray(charset(UTF8)), 20
                )
            )

            String(pbeCipher.doFinal(bytes), Charsets.UTF_8)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    companion object {
        // UTF 8 Constant
        private const val UTF8 = "utf-8"

        // Constant Password (Change Password as Application Name)
        private const val PASSWORD = "triptiva-eSignature"

        // Array of Password String Character
        private val SECRIT = PASSWORD.toCharArray()

        // ObSecure Preferences
        private var preference: ObscureSharePreference? = null

        //Set to true if a decryption error was detected in the case of float, int, and long we can tell if there was a parse error
        //this does not detect an error in strings or boolean - that requires more sophisticated checks
        var decryptionErrorFlag = false

        /***
         * Accessor to grab the preferences in a singleton.
         * This stores the reference in a singleton so it can be accessed repeatedly with
         * no performance penalty
         * @param c           - the context used to access the preferences.
         * @param appName     - domain the shared preferences should be stored under
         * @param contextMode - Typically Context.MODE_PRIVATE
         * @return
         */
        @Synchronized
        fun getPrefs(c: Context, appName: String?, contextMode: Int): ObscureSharePreference? {
            if (preference == null) {
                //make sure to use application context since preferences live outside an Activity
                //use for objects that have global scope like: prefs or starting services
                preference = ObscureSharePreference(
                    c.applicationContext,
                    c.applicationContext.getSharedPreferences(appName, contextMode)
                )
            }

            return preference
        }
    }
}