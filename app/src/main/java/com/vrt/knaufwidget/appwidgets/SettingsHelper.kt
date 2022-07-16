package com.vrt.knaufwidget.appwidgets

import android.content.Context
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.icu.util.Calendar


object SettingsHelper {

    private const val PREF_NAME = "KNAUF_WIDGET_SETTINGS"
    private const val COLOR_SCHEME_KEY = "KNAUF_WIDGET_SETTINGS_COLOR_SCHEME_KEY"
    private const val MONTH_KEY = "KNAUF_WIDGET_SETTINGS_MONTH_KEY"
    private const val YEAR_KEY = "KNAUF_WIDGET_SETTINGS_YEAR_KEY"
    private const val ACC_KEY = "KNAUF_WIDGET_SETTINGS_ACC_KEY"
    private const val TOKEN_KEY = "KNAUF_WIDGET_SETTINGS_TOKEN_KEY"

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

    fun saveColorScheme(context: Context, scheme: ColorSchema) {
        val settings: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        settings.edit().apply {
            putInt(COLOR_SCHEME_KEY, scheme.schemeID)
            apply()
        }
    }

    fun getSavedColorScheme(context: Context) : ColorSchema {
        val settings: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        return when (settings.getInt(COLOR_SCHEME_KEY, -1)) {
            2 -> ColorSchema.Schema2
            3 -> ColorSchema.Schema3
            4 -> ColorSchema.Schema4
            else -> ColorSchema.Schema1
        }
    }

    fun saveAccForSync(context: Context, acc: String) {
        val settings: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        settings.edit().apply {
            putString(ACC_KEY, acc)
            apply()
        }
    }

    fun getSavedAccForSync(context: Context) : String {
        val settings: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        return settings.getString(ACC_KEY, "") ?: ""
    }

    fun saveOutlookToken(context: Context, token: String) {
        val settings: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        settings.edit().apply {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getSavedOutlookToken(context: Context) : String? {
        val settings: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        return settings.getString(TOKEN_KEY, null)
    }
}