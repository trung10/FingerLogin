package com.example.login.di

import android.annotation.SuppressLint
import android.content.Context
import com.example.login.domain.prefs.SetUpPassCodeCompletedUseCase
import com.example.login.ui.login.SplashFragment
import com.example.login.ui.login.SplashViewModel

@SuppressLint("StaticFieldLeak")
object InjectionUtil {
    private lateinit var context: Context


    val setUpPassCodeCompletedUseCase: SetUpPassCodeCompletedUseCase by lazy {
        SetUpPassCodeCompletedUseCase(preferenceStorage = )
    }

    fun inject(fragment: SplashFragment) {
        context = fragment.activity!!.applicationContext
        fragment.createTodo = createTodo
        fragment.getAllTodo = getAllTodo
        fragment.viewModel = SplashViewModel(setUpPassCodeCompletedUseCase = )
    }
}