package com.vrt.knaufwidget.appwidgets.fx

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.vrt.knaufwidget.*
import com.vrt.knaufwidget.appwidgets.*
import org.json.JSONObject


class KnaufWidgetFX : AppWidgetProvider() {

    companion object {
        const val SCHEMA_UPDATE_KEY = "SCHEMA_UPDATE_FX_KEY"
    }

    private val mainBG = R.id.fxRoot
    private val timeText = R.id.timeTextFX
    private val dateText = R.id.dateTextFX
    private val logo = R.id.logoFX
    private val curTitle = R.id.curTitle
    private val rateTitle = R.id.rateTitle
    private val diffTitle = R.id.diffTitle
    private val exchangeContainer = R.id.exchangeContainer

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        context ?: return
        appWidgetManager ?: return
        appWidgetIds?.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
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
        val manager = AppWidgetManager.getInstance(context)
        manager.getAppWidgetIds(ComponentName(context, KnaufWidgetFX::class.java)).forEach {
            askFX(context, CurrencyType.RUB, it, javaClass)
            askFX(context, CurrencyType.USD, it, javaClass)
            askFX(context, CurrencyType.EUR, it, javaClass)
        }

        val action = intent?.action ?: return
        if (action == IntentType.OpenClock.ACTION_OPEN_CLOCK) {
            startClock(context)
        }
        if (action == IntentType.OpenFX.ACTION_OPEN_FX) {
            startFX(context)
        }

        if (action == IntentType.ChangeColorSchema.ACTION_CHANGE_COLOR_SCHEMA) {
            val remoteViews = RemoteViews(context.packageName, R.layout.knauf_widget_fx)
            val wID = intent.getStringExtra(IntentType.PrevClick.WIDGET_INFO_ID) ?: return
            updateColorSchema(remoteViews, context, wID.toInt())
        }

        if (action == MainActivity.UPDATE_FROM_ACTIVITY) {
            val remoteViews = RemoteViews(context.packageName, R.layout.knauf_widget_fx)
            val wIDs = intent.getIntArrayExtra(SCHEMA_UPDATE_KEY) ?: return
            wIDs.forEach { wID ->
                updateColorSchema(remoteViews, context, wID)
            }
        }

        if (action == IntentType.FXExc.ACTION_FX_EXC) {
            val fxExcInfoString = intent.getStringExtra(IntentType.FXExc.FX_EXC_INFO_KEY) ?: return
            val fxExcInfo: FXExceptionEntity = JSONObject(fxExcInfoString).toClassObject()
            fillInfo(fxExcInfo, context)
        }

        if (action == IntentType.FXEr.ACTION_FX_ER) {
            val fxErInfoString = intent.getStringExtra(IntentType.FXEr.FX_ER_INFO_KEY) ?: return
            val fxErInfo: FXErrorEntity = JSONObject(fxErInfoString).toClassObject()
            fillInfo(fxErInfo, context)
        }

        val fxInfoString = intent.getStringExtra(IntentType.NewFX.FX_INFO_KEY) ?: return
        val fxInfo: FXEntity = JSONObject(fxInfoString).toClassObject()
        fillInfo(fxInfo, context)
    }

    private fun updateColorSchema(views: RemoteViews, context: Context, widgetID: Int) {
        val schema = SettingsHelper.getSavedColorScheme(context)
        views.setInt(mainBG, ColorSchemaNew.SET_BACKGROUND_COLOR_KEY, schema.mainBg.invoke(context))
        views.setTextColor(dateText, schema.clockText.invoke(context))
        views.setTextColor(timeText, schema.clockText.invoke(context))
        views.setImageViewResource(logo, schema.logo)

        views.setInt(curTitle, ColorSchemaNew.SET_BACKGROUND_COLOR_KEY, schema.titleFXBg.invoke(context))
        views.setInt(rateTitle, ColorSchemaNew.SET_BACKGROUND_COLOR_KEY, schema.titleFXBg.invoke(context))
        views.setInt(diffTitle, ColorSchemaNew.SET_BACKGROUND_COLOR_KEY, schema.titleFXBg.invoke(context))
        views.setInt(exchangeContainer, ColorSchemaNew.SET_BACKGROUND_COLOR_KEY, schema.contentFXBg.invoke(context))

        AppWidgetManager.getInstance(context).updateAppWidget(widgetID, views)
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

        val pendingOpenClockIntent = buildIntent(IntentType.OpenClock, appWidgetId, context, javaClass)
        val pendingOpenFXIntent = buildIntent(IntentType.OpenFX, appWidgetId, context, javaClass)
        val views = RemoteViews(context.packageName, R.layout.knauf_widget_fx)
        val w = appWidgetManager.getAppWidgetOptions(appWidgetId).getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
        scaleText(views, w, getCurDateForClock(), WidgetState.FX)
        updateColorSchema(views, context, appWidgetId)
        views.setOnClickPendingIntent(R.id.clockContainerFX, pendingOpenClockIntent)
        views.setOnClickPendingIntent(R.id.exchangeContainer, pendingOpenFXIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
        askFX(context, CurrencyType.RUB, appWidgetId, javaClass)
        askFX(context, CurrencyType.USD, appWidgetId, javaClass)
        askFX(context, CurrencyType.EUR, appWidgetId, javaClass)
    }
}