package com.example.login.domain.prefs

import com.example.login.data.prefs.PreferenceStorage
import com.example.login.domain.UseCase

class CheckPassCode constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Unit, Boolean>() {
    override fun execute(parameters: Unit): Boolean = false
}