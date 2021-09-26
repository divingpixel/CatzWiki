package com.epikron.catzwiki.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(val context: Context) {

    companion object {
        private const val APP_PREFS = "app_preferences"
    }

    private val appPrefs: SharedPreferences = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)

    fun clear() = with(appPrefs.edit()) {
        clear()
        apply()
    }
}
