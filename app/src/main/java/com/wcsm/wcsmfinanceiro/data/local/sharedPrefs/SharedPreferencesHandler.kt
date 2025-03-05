package com.wcsm.wcsmfinanceiro.data.local.sharedPrefs

import android.app.Activity
import android.content.Context

class SharedPreferencesHandler(private val activity: Activity?) {

    fun savePreference(preferenceId: String, preference: Boolean) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(preferenceId, preference)
            apply()
        }
    }

    fun getBooleanPreference(preferenceId: String, defaultValue: Boolean) : Boolean {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return defaultValue
        return sharedPref.getBoolean(preferenceId, defaultValue)
    }
}