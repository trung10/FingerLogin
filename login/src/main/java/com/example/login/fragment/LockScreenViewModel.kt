package com.example.login.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.login.R
import com.example.login.domain.prefs.CheckPassCode
import com.example.login.domain.prefs.StorageHashPassCode

class LockScreenViewModel constructor(
    storageHashPassCode: StorageHashPassCode,
    checkPassCode: CheckPassCode
) : ViewModel() {
    private val _hashCode = MutableLiveData<String>()
    val hashCode: LiveData<String> = _hashCode

    private val _status = MutableLiveData<Int>()
    val status: LiveData<Int> = _status

    private val _shake = MutableLiveData<Boolean>()
    val shake: LiveData<Boolean> = _shake

    private val _isFingerPrint = MutableLiveData<Boolean>()
    val isFingerPrint: LiveData<Boolean> = _isFingerPrint

    init {
        _hashCode.value = ""
        _status.value = R.string.input_your_pass_code
    }
}