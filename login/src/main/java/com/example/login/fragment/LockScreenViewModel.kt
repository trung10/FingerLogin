package com.example.login.fragment

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
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

    private val _fingerPrinVisiable = MutableLiveData<Int>()
    val fingerPrinVisiable: LiveData<Int> = _fingerPrinVisiable

    private val passCode: String = ""

    init {
        _hashCode.value = ""
        _status.value = R.string.input_your_pass_code
        _isFingerPrint.value = true
        _fingerPrinVisiable.value = View.VISIBLE
    }

    fun onKeyClick0() {
        Log.e("AAAAAAAAAAA", "0")
    }

    fun onKeyClick1() {
        Log.e("AAAAAAAAAAA", "1")
    }

    fun onKeyClick2() {
        Log.e("AAAAAAAAAAA", "2")
    }

    fun onKeyClick3() {
        Log.e("AAAAAAAAAAA", "3")
    }

    fun onKeyClick4() {
        Log.e("AAAAAAAAAAA", "4")
    }

    fun onKeyClick5() {
        Log.e("AAAAAAAAAAA", "5")
    }

    fun onKeyClick6() {
        Log.e("AAAAAAAAAAA", "6")
    }

    fun onKeyClick7() {
        Log.e("AAAAAAAAAAA", "7")
    }

    fun onKeyClick8() {
        Log.e("AAAAAAAAAAA", "8")
    }

    fun onKeyClick9() {
        Log.e("AAAAAAAAAAA", "9")
    }
}

@BindingAdapter("textResId")
fun loadImage(view: TextView, resId: Int) {
    view.text = view.resources.getText(resId)
}


enum class Mode {
    MODE_CREATE,
    MODE_AUTH
}