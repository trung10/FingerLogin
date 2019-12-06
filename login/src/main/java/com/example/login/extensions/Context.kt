package com.example.login.extensions

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.login.helpers.BaseConfig
import com.example.login.helpers.PREFS_KEY
import com.example.login.helpers.isMarshmallowPlus
import com.example.login.helpers.isOnMainThread
import com.github.ajalt.reprint.core.Reprint

fun Context.getSharedPrefs() = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

val Context.baseConfig: BaseConfig get() = BaseConfig.newInstance(applicationContext)

fun Context.isFingerPrintSensorAvailable() = isMarshmallowPlus() && Reprint.isHardwarePresent()

fun Context.isFingerprintsExists() = Reprint.hasFingerprintRegistered()

fun Context.toast(id: Int, length: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), length)
}

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    try {
        if (isOnMainThread()) {
            doToast(this, msg, length)
        } else {
            Handler(Looper.getMainLooper()).post {
                doToast(this, msg, length)
            }
        }
    } catch (e: Exception) {
    }
}

private fun doToast(context: Context, message: String, length: Int) {
    if (context is Activity) {
        if (!context.isFinishing && !context.isDestroyed) {
            Toast.makeText(context, message, length).show()
        }
    } else {
        Toast.makeText(context, message, length).show()
    }
}