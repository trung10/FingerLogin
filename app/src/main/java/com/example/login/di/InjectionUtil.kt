package com.example.login.di

import android.annotation.SuppressLint
import android.content.Context
import com.example.login.data.prefs.PreferenceStorage
import com.example.login.data.prefs.SharedPreferenceStorage
import com.example.login.domain.prefs.SetUpPassCodeCompletedUseCase
import com.example.login.ui.login.SplashFragment
import com.example.login.ui.login.SplashViewModel

@SuppressLint("StaticFieldLeak")
object InjectionUtil {
    private lateinit var context: Context


    private val setUpPassCodeCompletedUseCase: SetUpPassCodeCompletedUseCase by lazy {
        SetUpPassCodeCompletedUseCase(preferenceStorage = preferenceStorage)
    }

    private val preferenceStorage: PreferenceStorage by lazy {
        SharedPreferenceStorage(context)
    }

    fun setup(context: Context) {
        this.context = context
    }

    fun inject(fragment: SplashFragment) {
        fragment.viewModel =
            SplashViewModel(setUpPassCodeCompletedUseCase = setUpPassCodeCompletedUseCase)
    }
}