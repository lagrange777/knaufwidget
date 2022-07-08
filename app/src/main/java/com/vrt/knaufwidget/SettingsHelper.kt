package com.vrt.knaufwidget

import android.content.Context
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.icu.util.Calendar


object SettingsHelper {

    private const val PREF_NAME = "KNAUF_WIDGET_SETTINGS"
    private const val MONTH_KEY = "KNAUF_WIDGET_SETTINGS_MONTH_KEY"
    private const val YEAR_KEY = "KNAUF_WIDGET_SETTINGS_YEAR_KEY"

    fun saveCurrentCalendarState(context: Context, month: Int, year: Int) {
        val settings: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        settings.edit().apply {
            putInt(MONTH_KEY, month)
            putInt(YEAR_KEY, year)
            apply()
        }
    }

    fun getSavedCalendarState(context: Context): Pair<Int, Int> {
        val settings: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        var year = settings.getInt(YEAR_KEY, -1)
        var month = settings.getInt(MONTH_KEY, -1)

        if (year == -1 || month == -1) {
            val calendar = Calendar.getInstance()
            month = calendar.get(java.util.Calendar.MONTH)
            year = calendar.get(java.util.Calendar.YEAR)
        }
        return month to year
    }
}