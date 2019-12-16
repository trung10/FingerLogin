package com.example.login.domain.prefs

import com.example.login.data.prefs.PreferenceStorage
import com.example.login.domain.UseCase

class StorageHashPassCode constructor(
    private val preferenceStorage: PreferenceStorage
): UseCase<Unit, Unit>() {
    override fun execute(parameters: Unit) {

    }

}