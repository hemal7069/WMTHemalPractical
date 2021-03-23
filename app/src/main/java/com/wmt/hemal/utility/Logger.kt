package com.wmt.hemal.utility

import android.util.Log

/***
 * Class Which helps to print log message.
 * This Class help to stop all application log print form one place.
 * For that just change debug boolean value false.
 * If debug value is true than all application log will print otherwise log will not print.
 * Here method name suggest where log will print:
 * v        -       Verbose
 * d        -       Debug
 * i        -       Info
 * w        -       Warn
 * e        -       Error
 */
object Logger {
    private const val debug = true
    private const val TRACE_CALLER_COUNT = 2

    /***
     * To print Log in Verbose Log with only Class and Function name
     */
    fun v() {
        if (debug) {
            Log.v(className, functionName)
        }
    }

    /***
     * To print verbose message in Verbose Log with Class and Function Name
     * @param msg       -       Message Which is print on Verbose Log
     */
    fun v(msg: String?) {
        if (debug) {
            Log.v(className, functionName + ", " + nonNull(msg))
        }
    }

    /***
     * To print Log in Debug Log with only Class and Function name
     */
    fun d() {
        if (debug) {
            Log.d(className, functionName)
        }
    }

    /***
     * To print debug message in Debug Log with Class and Function name
     * @param msg       -       Message which is print on Log
     */
    fun d(msg: String?) {
        if (debug) {
            Log.d(className, functionName + ", " + nonNull(msg))
        }
    }

    /***
     * To Print Info Log with Only Class and Function Name
     */
    fun i() {
        if (debug) {
            Log.i(className, functionName)
        }
    }

    /***
     * To Print Informative Message in Info Log with Class and Function name
     * @param msg       -       Message Which is print on Info Log
     */
    fun i(msg: String?) {
        if (debug) {
            Log.i(className, functionName + ", " + nonNull(msg))
        }
    }

    /***
     * To print Warning Message in Warn Log with Class and Function name
     * @param msg       -       Warning message show in log
     */
    fun w(msg: String?) {
        if (debug) {
            Log.w(className, functionName + ", " + nonNull(msg))
        }
    }

    /***
     * To Print Warning Message with Throwable in Warn Log with Class and Function Name
     * @param msg       -       Warning message which print on Warn Log
     * @param e         -       Throwable error message
     */
    fun w(msg: String?, e: Throwable?) {
        if (debug) {
            Log.w(className, functionName + ", " + nonNull(msg), e)
        }
    }

    /***
     * To Print Error Message in Error Log with Class and Function Name
     * @param msg       -       Error Message which print on Error Log
     */
    fun e(msg: String?) {
        if (debug) {
            Log.e(className, functionName + ", " + nonNull(msg))
        }
    }

    /***
     * To Print Error Message with Throwable Error in Error Log with Class and Function Name.
     * @param msg       -       Error Message which print on Error Log
     * @param e         -       Throwable Error Message
     */
    fun e(msg: String?, e: Throwable?) {
        if (debug) {
            Log.e(className, functionName + ", " + nonNull(msg), e)
        }
    }

    /***
     * To return not null String.
     * @param s     -       String which is check null or not
     * @return      -       returns '(null)' if string is null otherwise return string.
     */
    private fun nonNull(s: String?): String {
        return s ?: "(null)"
    }

    /***
     * To get Class name where this Logger Class is used.
     * @return      -       Class name
     */
    private val className: String
        get() {
            var fn = ""

            try {
                fn = Throwable().stackTrace[TRACE_CALLER_COUNT].className
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return fn
        }

    /***
     * To get function name where this Logger class is used.
     * @return      -       Function Name
     */
    private val functionName: String
        get() {
            var fn = ""

            try {
                fn = Throwable().stackTrace[TRACE_CALLER_COUNT].methodName
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return fn
        }
}