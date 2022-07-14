package com.vrt.knaufwidget.appwidgets.clock

import android.Manifest
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.vrt.knaufwidget.MainActivity
import com.vrt.knaufwidget.R
import com.vrt.knaufwidget.appwidgets.*
import com.vrt.knaufwidget.startClock
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class KnaufWidgetClock : AppWidgetProvider() {

    companion object {
        const val SCHEMA_UPDATE_KEY = "SCHEMA_UPDATE_CLOCK_KEY"
    }

    private val mainBG = R.id.clockRoot
    private val timeText = R.id.timeTextCl
    private val dateText = R.id.dateTextCl
    private val logo = R.id.logoCl

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        context ?: return
        appWidgetManager ?: return
        appWidgetIds?.forEach { appWidgetId -> updateWidget(context, appWidgetManager, appWidgetId) }
    }

    override fun onAppWidgetOptionsChanged(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle?) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        context ?: return
        appWidgetManager ?: return
        updateWidget(context, appWidgetManager, appWidgetId)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: return
        val action = intent?.action ?: return
        if (action == IntentType.OpenClock.ACTION_OPEN_CLOCK) {
            startClock(context)
        }

        if (action == MainActivity.UPDATE_FROM_ACTIVITY) {
            val remoteViews = RemoteViews(context.packageName, R.layout.knauf_widget_clock)
            val wIDs = intent.getIntArrayExtra(SCHEMA_UPDATE_KEY) ?: return
            wIDs.forEach { wID ->
                updateColorSchema(remoteViews, context, wID)
            }
        }
    }

    private fun updateColorSchema(views: RemoteViews, context: Context, widgetID: Int) {
        val schema = SettingsHelper.getSavedColorScheme(context)
        views.setInt(mainBG, ColorSchema.SET_BACKGROUND_COLOR_KEY, schema.mainBg.invoke(context))

        views.setTextColor(dateText, schema.clockText.invoke(context))
        views.setTextColor(timeText, schema.clockText.invoke(context))

        views.setImageViewResource(logo, schema.logo)
        AppWidgetManager.getInstance(context).updateAppWidget(widgetID, views)
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        println("CLOCKWIDGET update")
//        if (!(MainActivity.checkPermission(
//                context,
//                MainActivity.CALLBACK_ID,
//                listOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
//            ))) return
        val pendingOpenClockIntent = buildIntent(IntentType.OpenClock, appWidgetId, context, javaClass)
        val views = RemoteViews(context.packageName, R.layout.knauf_widget_clock)
        val w = appWidgetManager.getAppWidgetOptions(appWidgetId).getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
        scaleText(views, w, getCurDateForClock(), WidgetState.Clock)
        views.setOnClickPendingIntent(R.id.clockContainerCl, pendingOpenClockIntent)
        updateColorSchema(views, context, appWidgetId)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}