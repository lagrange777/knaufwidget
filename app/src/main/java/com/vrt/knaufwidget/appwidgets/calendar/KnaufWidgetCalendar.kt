package com.vrt.knaufwidget.appwidgets.calendar

import android.Manifest
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.RemoteViews
import com.vrt.knaufwidget.*
import com.vrt.knaufwidget.appwidgets.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        if (!(MainActivity.checkPermission(
                context,
                listOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
            ))
        ) return
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
                updateWidget(context, AppWidgetManager.getInstance(context), wID.toInt(), false)
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
                updateWidget(context, AppWidgetManager.getInstance(context), wID.toInt(), false)
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
        val langCode = SettingsHelper.getSavedTranslationCode(context)
        views.setInt(mainBG, ColorSchemaNew.SET_BACKGROUND_COLOR_KEY, schema.mainBg.invoke(context))

        views.setImageViewResource(logo, schema.logo)
        views.setTextColor(dateText, schema.clockText.invoke(context))
        views.setTextColor(timeText, schema.clockText.invoke(context))
        views.setTextViewText(R.id.subTitle2, TranslationHelper.mondayLabel.invoke(langCode))
        views.setTextViewText(R.id.subTitle3, TranslationHelper.tuesdayLabel.invoke(langCode))
        views.setTextViewText(R.id.subTitle4, TranslationHelper.wednesdayLabel.invoke(langCode))
        views.setTextViewText(R.id.subTitle5, TranslationHelper.thursdayLabel.invoke(langCode))
        views.setTextViewText(R.id.subTitle6, TranslationHelper.fridayLabel.invoke(langCode))
        views.setTextViewText(R.id.subTitle7, TranslationHelper.saturdayLabel.invoke(langCode))
        views.setTextViewText(R.id.subTitle8, TranslationHelper.sundayLabel.invoke(langCode))
        val helper = KnaufMonth(context)
        views.setTextViewText(R.id.curYearText, helper.curTitle)

        views.setInt(selector, ColorSchemaNew.SET_BACKGROUND_COLOR_KEY, schema.titleCalBg.invoke(context))
        subTitles.forEach { views.setInt(it, ColorSchemaNew.SET_BACKGROUND_COLOR_KEY, schema.subtitleCalBg.invoke(context)) }
        views.setInt(calendar, ColorSchemaNew.SET_BACKGROUND_COLOR_KEY, schema.contentCalBg.invoke(context))

        AppWidgetManager.getInstance(context).updateAppWidget(widgetID, views)
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        shouldRefresh: Boolean = true
    ) {

        if (!(MainActivity.checkPermission(
                context,
                listOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
            ))
        ) return

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
        if (shouldRefresh) {
            pendingPrevClickIntent.send()
            pendingNextClickIntent.send()
        }
    }
}
