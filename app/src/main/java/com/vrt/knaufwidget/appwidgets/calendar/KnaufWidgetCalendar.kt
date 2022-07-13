package com.vrt.knaufwidget.appwidgets.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.vrt.knaufwidget.MainActivity
import com.vrt.knaufwidget.R
import com.vrt.knaufwidget.appwidgets.*
import com.vrt.knaufwidget.startClock
import com.vrt.knaufwidget.startNativeCalendar

class KnaufWidgetCalendar : AppWidgetProvider() {

    companion object {
        const val SCHEMA_UPDATE_KEY = "SCHEMA_UPDATE_CALENDAR_KEY"
    }

    private val mainBG = R.id.calendarRoot
    private val timeText = R.id.timeTextCal
    private val dateText = R.id.dateTextCal
    private val logo = R.id.logoCal

    private val selector = R.id.calendarSelector
    private val calendar = R.id.calendarContainer
    private val subTitles =
        listOf(
            R.id.subTitle1,
            R.id.subTitle2,
            R.id.subTitle3,
            R.id.subTitle4,
            R.id.subTitle5,
            R.id.subTitle6,
            R.id.subTitle7,
            R.id.subTitle8
        )


    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {

        super.onUpdate(context, appWidgetManager, appWidgetIds)
        context ?: return
        appWidgetManager ?: return
        appWidgetIds ?: return
        appWidgetIds.joinToString { "$it" }.apply { println("CALENDARUPDATE $this") }
        appWidgetIds.forEach { appWidgetId ->
            val pendingPrevClickIntent = buildIntent(IntentType.PrevClick, appWidgetId, context, javaClass, appWidgetId.toString())
            val pendingNextClickIntent = buildIntent(IntentType.NextClick, appWidgetId, context, javaClass, appWidgetId.toString())
            val pendingOpenCalendarIntent = buildIntent(IntentType.OpenCalendar, appWidgetId, context, javaClass)
            val pendingOpenClockIntent = buildIntent(IntentType.OpenClock, appWidgetId, context, javaClass)
            val views = RemoteViews(context.packageName, R.layout.knauf_widget_calendar)
            val w = appWidgetManager.getAppWidgetOptions(appWidgetId).getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)

            scaleText(views, w, getCurDateForClock(), WidgetState.Calendar)
            updateCalendar(context, appWidgetId)
            updateColorSchema(views, context, appWidgetId)
            views.setOnClickPendingIntent(R.id.clockContainerCal, pendingOpenClockIntent)
            views.setOnClickPendingIntent(R.id.calendarContainer, pendingOpenCalendarIntent)
            views.setOnClickPendingIntent(R.id.prevMonthBtn, pendingPrevClickIntent)
            views.setOnClickPendingIntent(R.id.nextMonthBtn, pendingNextClickIntent)


            views.setImageViewResource(R.id.prevMonthBtn, R.drawable.ic_arrow_back)
            views.setImageViewResource(R.id.nextMonthBtn, R.drawable.ic_arrow_forward)

            appWidgetManager.updateAppWidget(appWidgetId, views)

        }
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: return
        val action = intent?.action ?: return

        when (action) {
            IntentType.NextClick.ACTION_NEXT_MONTH_CLICK -> {
                SettingsHelper.getSavedCalendarState(context).apply {
                    var newY = second
                    val newM = if (first + 1 <= 11) first + 1 else {
                        newY += 1
                        0
                    }
                    SettingsHelper.saveCurrentCalendarState(context, newM, newY)
                }
                val wID = intent.getStringExtra(IntentType.PrevClick.WIDGET_INFO_ID) ?: return
                updateCalendar(context, wID.toInt())
            }
            IntentType.PrevClick.ACTION_PREV_MONTH_CLICK -> {
                SettingsHelper.getSavedCalendarState(context).apply {
                    var newY = second
                    val newM = if (first - 1 >= 0) first - 1 else {
                        newY -= 1
                        11
                    }
                    SettingsHelper.saveCurrentCalendarState(context, newM, newY)
                }
                val wID = intent.getStringExtra(IntentType.PrevClick.WIDGET_INFO_ID) ?: return
                updateCalendar(context, wID.toInt())
            }
            MainActivity.UPDATE_FROM_ACTIVITY -> {
                val remoteViews = RemoteViews(context.packageName, R.layout.knauf_widget_calendar)
                val wIDs = intent.getIntArrayExtra(SCHEMA_UPDATE_KEY) ?: return
                wIDs.forEach { wID ->
                    updateColorSchema(remoteViews, context, wID)
                }

            }
            IntentType.OpenCalendar.ACTION_OPEN_NATIVE_CALENDAR -> {
                startNativeCalendar(context)
            }
            IntentType.OpenClock.ACTION_OPEN_CLOCK -> {
                startClock(context)
            }
        }
    }

    private fun updateColorSchema(views: RemoteViews, context: Context, widgetID: Int) {
        val schema = SettingsHelper.getSavedColorScheme(context)
        views.setInt(mainBG, ColorSchema.SET_BACKGROUND_COLOR_KEY, schema.mainBg.invoke(context))

        views.setImageViewResource(logo, schema.logo)
        views.setTextColor(dateText, schema.clockText.invoke(context))
        views.setTextColor(timeText, schema.clockText.invoke(context))

        views.setInt(selector, ColorSchema.SET_BACKGROUND_COLOR_KEY, schema.titleBg.invoke(context))
        subTitles.forEach { views.setInt(it, ColorSchema.SET_BACKGROUND_COLOR_KEY, schema.subtitleBg.invoke(context)) }
        views.setInt(calendar, ColorSchema.SET_BACKGROUND_COLOR_KEY, schema.contentCalBg.invoke(context))

        AppWidgetManager.getInstance(context).updateAppWidget(widgetID, views)
    }
}