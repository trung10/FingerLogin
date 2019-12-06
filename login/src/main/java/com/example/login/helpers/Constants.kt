package com.example.login.helpers

import android.os.Build
import android.os.Looper

fun isMarshmallowPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
fun isNougatPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
fun isNougatMR1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1
fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
fun isPiePlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

const val PREFS_KEY = "Prefs"
const val APP_PASSWORD_PROTECTION = "app_password_protection"
const val APP_PASSWORD_HASH = "app_password_hash"

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()

