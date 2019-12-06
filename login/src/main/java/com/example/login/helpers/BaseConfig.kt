package com.example.login.helpers

import android.content.Context
import com.example.login.extensions.getSharedPrefs

open class BaseConfig(val context: Context) {

    companion object {
        fun newInstance(context: Context) = BaseConfig(context)
    }

    protected val prefs = context.getSharedPrefs()

    var isAppPasswordProtectionOn: Boolean
        get() = prefs.getBoolean(APP_PASSWORD_PROTECTION, false)
        set(isAppPasswordProtectionOn) = prefs.edit().putBoolean(
            APP_PASSWORD_PROTECTION,
            isAppPasswordProtectionOn
        ).apply()

    var appPasswordHash: String
        get() = prefs.getString(APP_PASSWORD_HASH, "")!!
        set(appPasswordHash) = prefs.edit().putString(APP_PASSWORD_HASH, appPasswordHash).apply()
}