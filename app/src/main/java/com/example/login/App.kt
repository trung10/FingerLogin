package com.example.login

import android.app.Application
import com.example.login.di.InjectionUtil
import com.github.ajalt.reprint.core.Reprint

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        InjectionUtil.setup(this)
        Reprint.initialize(this)
    }
}