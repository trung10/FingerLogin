package com.example.login.domain.prefs

import com.example.login.data.prefs.PreferenceStorage
import com.example.login.domain.UseCase

class CheckPassCode constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<String, Boolean>() {
    override fun execute(parameters: String): Boolean {
        return if (parameters.isBlank()) {
            false
        } else {
            parameters == preferenceStorage.hashPassCode
        }
    }
}