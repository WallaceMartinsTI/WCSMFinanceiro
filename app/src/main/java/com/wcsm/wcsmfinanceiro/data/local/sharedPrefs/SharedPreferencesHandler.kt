package com.wcsm.wcsmfinanceiro.data.local.sharedPrefs

import android.content.Context

class SharedPreferencesHandler(private val context: Context) {
    private val sharedPref = context.getSharedPreferences("wcsm_financeiro_prefs", Context.MODE_PRIVATE)

    fun savePreference(preferenceId: String, preference: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(preferenceId, preference)
            apply()
        }
    }

    fun getBooleanPreference(preferenceId: String, defaultValue: Boolean) : Boolean {
        return sharedPref.getBoolean(preferenceId, defaultValue)
    }
}