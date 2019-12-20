package com.example.login.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.login.data.prefs.PreferenceStorage
import com.example.login.data.prefs.SharedPreferenceStorage
import com.example.login.domain.prefs.CheckPassCode
import com.example.login.domain.prefs.SetUpPassCodeCompletedUseCase
import com.example.login.domain.prefs.StorageHashPassCode
import com.example.login.fragment.LockScreenFragment
import com.example.login.fragment.LockScreenViewModel

@SuppressLint("StaticFieldLeak")
object InjectionUtil {
    private lateinit var context: Context

    private val preferenceStorage: PreferenceStorage by lazy {
        SharedPreferenceStorage(context)
    }

    fun setup(context: Context) {
        this.context = context
    }

    private val storageHashPassCode: StorageHashPassCode by lazy {
        StorageHashPassCode(preferenceStorage = preferenceStorage)
    }

    private val checkPassCode: CheckPassCode by lazy {
        CheckPassCode(preferenceStorage = preferenceStorage)
    }

    fun inject(fragment: LockScreenFragment) {
        fragment.viewModel = LockScreenViewModel(
            storageHashPassCode = storageHashPassCode,
            checkPassCode = checkPassCode
        )
    }

    class myViewModelFactory: ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.newInstance()
        }
    }
}