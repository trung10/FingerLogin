package com.example.login.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.login.domain.prefs.SetUpPassCodeCompletedUseCase
import com.example.login.result.Event
import com.example.login.result.Result
import com.example.login.util.map


class SplashViewModel(setUpPassCodeCompletedUseCase: SetUpPassCodeCompletedUseCase) : ViewModel() {

    private val onboardingCompletedResult = MutableLiveData<Result<Boolean>>()
    val splashDestination: LiveData<Event<SplashDestination>>

    init {
        // Check if onboarding has already been completed and then navigate the user accordingly
        setUpPassCodeCompletedUseCase(Unit, onboardingCompletedResult)
        splashDestination = onboardingCompletedResult.map {
            // If this check fails, prefer to launch main activity than show onboarding too often
            if ((it as? Result.Success)?.data == false) {
                Event(SplashDestination.SET_UP_PASS_CODE)
            } else {
                Event(SplashDestination.CONFIRM_PASS_CODE)
            }
        }
    }
}

enum class SplashDestination {
    SET_UP_PASS_CODE,
    CONFIRM_PASS_CODE
}