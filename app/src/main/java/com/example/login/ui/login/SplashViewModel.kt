package com.example.login.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {
    val isShowDialogFinger = MutableLiveData<Boolean>(true)

    val isFirstRunApp = MutableLiveData<Boolean>(false)

    init {

    }
}
