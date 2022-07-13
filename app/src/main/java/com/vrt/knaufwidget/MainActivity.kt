package com.vrt.knaufwidget

import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vrt.knaufwidget.appwidgets.ColorSchema
import com.vrt.knaufwidget.appwidgets.IntentType
import com.vrt.knaufwidget.appwidgets.SettingsHelper
import com.vrt.knaufwidget.appwidgets.buildIntent
import com.vrt.knaufwidget.appwidgets.calendar.KnaufWidgetCalendar
import com.vrt.knaufwidget.appwidgets.clock.KnaufWidgetClock
import com.vrt.knaufwidget.appwidgets.fx.KnaufWidgetFX


class MainActivity : AppCompatActivity() {

    companion object {
        const val CALLBACK_ID = 42
        const val UPDATE_FROM_ACTIVITY = "android.appwidget.action.APPWIDGET_UPDATE"
        private const val APP_ID = """4779bfe3-69a7-4335-a003-7b28ae1bcd71"""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission(CALLBACK_ID, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        val res = UtilityTest().readCalendarEvent(this)
        initButtons()
        updateTitle()
    }

    private fun checkPermission(callbackId: Int, vararg permissionsId: String) {
        var permissions = true
        for (p in permissionsId) {
            permissions =
                permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED
        }
        if (!permissions) ActivityCompat.requestPermissions(this, permissionsId, callbackId)
    }

    private fun initButtons() {
        val b1 = findViewById<AppCompatButton>(R.id.buttonS1)
        val b2 = findViewById<AppCompatButton>(R.id.buttonS2)
        val b3 = findViewById<AppCompatButton>(R.id.buttonS3)
        val b4 = findViewById<AppCompatButton>(R.id.buttonS4)

        b1.setOnClickListener { onSchemaBtnClick(ColorSchema.Schema1)}
        b2.setOnClickListener { onSchemaBtnClick(ColorSchema.Schema2)}
        b3.setOnClickListener { onSchemaBtnClick(ColorSchema.Schema3)}
        b4.setOnClickListener { onSchemaBtnClick(ColorSchema.Schema4)}
    }

    private fun onSchemaBtnClick(schema: ColorSchema) {
        SettingsHelper.saveColorScheme(this, schema)
        notifyAllWidgets()
        updateTitle()
    }

    private fun updateTitle() {
        val t = findViewById<AppCompatTextView>(R.id.schemaTitle)
        SettingsHelper.getSavedColorScheme(this).let {
            t.text = "Выбрана цветовая схема ${it.schemeID}"
        }
    }

    private fun notifyAllWidgets() {
        val manager = AppWidgetManager.getInstance(this)
        val intent = Intent(UPDATE_FROM_ACTIVITY)
        val clockIds = manager.getAppWidgetIds(ComponentName(this, KnaufWidgetClock::class.java))
        val fxIds = manager.getAppWidgetIds(ComponentName(this, KnaufWidgetFX::class.java))
        val calendarIds = manager.getAppWidgetIds(ComponentName(this, KnaufWidgetCalendar::class.java))
        intent.putExtra(KnaufWidgetClock.SCHEMA_UPDATE_KEY, clockIds)
        intent.putExtra(KnaufWidgetFX.SCHEMA_UPDATE_KEY, fxIds)
        intent.putExtra(KnaufWidgetCalendar.SCHEMA_UPDATE_KEY, calendarIds)
        this.sendBroadcast(intent)


    }


}