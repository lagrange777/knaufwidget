package com.vrt.knaufwidget.appwidgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.RemoteViews
import com.vrt.knaufwidget.R
import java.text.SimpleDateFormat
import java.util.*

//fun resolveVisibility(h: Int, remoteViews: RemoteViews) {
//    val state =
//        when {
//            h < 200 -> WidgetState.Clock
//            h in 200..300 -> WidgetState.Exchange
//            else -> WidgetState.Calendar
//        }
//    remoteViews.setViewVisibility(R.id.clockContainerCl, state.clockVisibility)
//    remoteViews.setViewVisibility(R.id.exchangeContainer, state.exchangeVisibility)
//    remoteViews.setViewVisibility(R.id.calendarSelector, state.calendarVisibility)
//    remoteViews.setViewVisibility(R.id.calendarContainer, state.calendarVisibility)
//}

fun scaleText(remoteViews: RemoteViews, maxWidth: Int, time: Pair<String, String>, state: WidgetState) {
    val newSizeTime = calc(time.first, maxWidth / 2 - 16)
    val newSizeDate = calc(time.second, maxWidth / 2 - 16)


    remoteViews.setTextViewTextSize(state.timeViewID, TypedValue.COMPLEX_UNIT_PX, newSizeTime)
    remoteViews.setTextViewTextSize(state.dateViewID, TypedValue.COMPLEX_UNIT_PX, newSizeDate)
}

fun getCurDateForClock(): Pair<String, String> {
    val t = Date()
    val tf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return tf.format(t) to df.format(t)
}

fun calc(text: String, maxWidth: Int): Float {
    val p = Paint()
    val r = Rect()
    var w = 0

    var res = 1f

    p.typeface = Typeface.DEFAULT
    while (true) {
        p.textSize = res
        p.getTextBounds(text, 0, text.count(), r)
        w = r.width()
        if (w >= maxWidth)
            break
        res++
    }
    return res
}

fun <T> buildIntent(
    intentType: IntentType,
    appWidgetId: Int,
    context: Context,
    javaClass: Class<T>,
    infoString: String? = null
): PendingIntent {
    val intent = Intent(context, javaClass)
    when (intentType) {
        IntentType.PrevClick -> {
            intent.action = IntentType.PrevClick.ACTION_PREV_MONTH_CLICK
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            infoString?.let { info -> intent.putExtra(IntentType.PrevClick.WIDGET_INFO_ID, info) }
        }
        IntentType.NextClick -> {
            intent.action = IntentType.NextClick.ACTION_NEXT_MONTH_CLICK
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            infoString?.let { info -> intent.putExtra(IntentType.NextClick.WIDGET_INFO_ID, info) }
        }
        IntentType.NewFX -> {
            intent.action = IntentType.NewFX.ACTION_NEW_FX
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            infoString?.let { info -> intent.putExtra(IntentType.NewFX.FX_INFO_KEY, info) }
        }
        IntentType.OpenCalendar -> {
            intent.action = IntentType.OpenCalendar.ACTION_OPEN_NATIVE_CALENDAR
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        IntentType.OpenClock -> {
            intent.action = IntentType.OpenClock.ACTION_OPEN_CLOCK
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        IntentType.ChangeColorSchema -> {
            intent.action = IntentType.ChangeColorSchema.ACTION_CHANGE_COLOR_SCHEMA
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
    }
    return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}