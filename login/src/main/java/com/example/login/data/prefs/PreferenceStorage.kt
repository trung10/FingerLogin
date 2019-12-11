package com.example.login.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.*
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Storage for app and user preferences.
 */
interface PreferenceStorage {
    var setUpPassCodeCompleted: Boolean
    var scheduleUiHintsShown: Boolean
    var notificationsPreferenceShown: Boolean
    var preferToReceiveNotifications: Boolean
    var myLocationOptedIn: Boolean
    var snackbarIsStopped: Boolean
    var observableSnackbarIsStopped: LiveData<Boolean>
    var sendUsageStatistics: Boolean
    var preferConferenceTimeZone: Boolean
    var selectedFilters: String?
    var selectedTheme: String?
    var observableSelectedTheme: LiveData<String>
    var codelabsInfoShown: Boolean
}

class SharedPreferenceStorage constructor(context: Context) : PreferenceStorage {

    private val prefs: Lazy<SharedPreferences> = lazy {
        // Lazy to prevent IO access to main thread.
        context.applicationContext.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        ).apply {
            registerOnSharedPreferenceChangeListener(changeListener)
        }
    }

    private val observableShowSnackbarResult = MutableLiveData<Boolean>()
    private val observableSelectedThemeResult = MutableLiveData<String>()

    private val changeListener = OnSharedPreferenceChangeListener { _, key ->
        {
            when (key) {
                PREF_SNACKBAR_IS_STOPPED -> observableShowSnackbarResult.value = snackbarIsStopped
                PREF_DARK_MODE_ENABLED -> observableSelectedThemeResult.value = selectedTheme
            }
        }
    }

    override var setUpPassCodeCompleted by BooleanPreference(prefs, PREF_SETTING_UP, false)

    override var scheduleUiHintsShown: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var notificationsPreferenceShown: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var preferToReceiveNotifications: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var myLocationOptedIn: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var snackbarIsStopped: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var observableSnackbarIsStopped: LiveData<Boolean>
        get() {
            observableShowSnackbarResult.value = snackbarIsStopped
            return observableShowSnackbarResult
        }
        set(_) = throw IllegalAccessException("Ec Ec Ec")
    override var sendUsageStatistics: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var preferConferenceTimeZone: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var selectedFilters: String?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var selectedTheme: String?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var observableSelectedTheme: LiveData<String>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var codelabsInfoShown: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}


    companion object {
        const val PREFS_NAME = "iosched"
        const val PREF_SETTING_UP = "pref_onboarding"
        const val PREF_SCHED_UI_HINTS_SHOWN = "pref_sched_ui_hints_shown"
        const val PREF_NOTIFICATIONS_SHOWN = "pref_notifications_shown"
        const val PREF_RECEIVE_NOTIFICATIONS = "pref_receive_notifications"
        const val PREF_MY_LOCATION_OPTED_IN = "pref_my_location_opted_in"
        const val PREF_SNACKBAR_IS_STOPPED = "pref_snackbar_is_stopped"
        const val PREF_SEND_USAGE_STATISTICS = "pref_send_usage_statistics"
        const val PREF_CONFERENCE_TIME_ZONE = "pref_conference_time_zone"
        const val PREF_SELECTED_FILTERS = "pref_selected_filters"
        const val PREF_DARK_MODE_ENABLED = "pref_dark_mode"
        const val PREF_CODELABS_INFO_SHOWN = "pref_codelabs_info_shown"
    }

    fun registerOnPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        prefs.value.registerOnSharedPreferenceChangeListener(listener)
    }
}

class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {
    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(
            name,
            defaultValue
        )//Todo: neu null -> crash -> bat no not null
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, defaultValue) }
    }
}

class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return preferences.value.getString(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}