package com.vrt.knaufwidget


import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews


/**
 * Implementation of App Widget functionality.
 */
class KnaufWidgetTime : AppWidgetProvider() {

    companion object {
        private const val ACTION_PREV_MONTH_CLICK = "PREV_MONTH"
        private const val ACTION_NEXT_MONTH_CLICK = "NEXT_MONTH"
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        context ?: return
        appWidgetManager ?: return
        appWidgetIds ?: return

        appWidgetIds.forEach { appWidgetId ->
            val pendingPrevClickIntent: PendingIntent = Intent(context, javaClass).let {
                it.action = ACTION_PREV_MONTH_CLICK
                it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                return@let PendingIntent.getBroadcast(context, appWidgetId, it, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            val pendingNextClickIntent: PendingIntent = Intent(context, javaClass).let {
                it.action = ACTION_NEXT_MONTH_CLICK
                it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                return@let PendingIntent.getBroadcast(context, appWidgetId, it, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            val views = RemoteViews(context.packageName, R.layout.knauf_widget_time)
            views.setOnClickPendingIntent(R.id.prevMonthBtn, pendingPrevClickIntent)
            views.setOnClickPendingIntent(R.id.nextMonthBtn, pendingNextClickIntent)

            views.setImageViewResource(R.id.imageView2, R.drawable.ic_vector)
            views.setImageViewResource(R.id.prevMonthBtn, R.drawable.ic_arrow_back)
            views.setImageViewResource(R.id.nextMonthBtn, R.drawable.ic_arrow_forward)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onEnabled(context: Context) {
        val remoteViews = RemoteViews(context.packageName, R.layout.knauf_widget_time)
        resolveVisibility(WidgetState.Clock, remoteViews)

    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        context ?: return
        appWidgetManager ?: return
        newOptions ?: return




        val maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)

        val remoteViews = RemoteViews(context.packageName, R.layout.knauf_widget_time)

        remoteViews.setImageViewResource(R.id.imageView2, R.drawable.ic_vector)

        val state = when  {
            maxHeight < 200 -> WidgetState.Clock
            maxHeight in 200..300 -> WidgetState.Exchange
            else -> WidgetState.Calendar
        }

        resolveVisibility(state, remoteViews)
        remoteViews.setTextViewCompoundDrawables(R.id.textView49, 0, 0, 0, R.drawable.red_dot )
        remoteViews.setTextViewCompoundDrawables(R.id.textView50, 0, 0, 0, R.drawable.red_dot )
        remoteViews.setTextViewCompoundDrawables(R.id.textView51, 0, 0, 0, R.drawable.red_dot )
        remoteViews.setTextViewCompoundDrawables(R.id.textView52, 0, 0, 0, R.drawable.red_dot )
        remoteViews.setTextViewCompoundDrawables(R.id.textView53, 0, 0, 0, R.drawable.red_dot )

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val action = intent?.action ?: return

        println("MILIWIDGET $action")

    }
}

sealed class WidgetState {
    abstract val clockVisibility: Int
    abstract val exchangeVisibility: Int
    abstract val calendarVisibility: Int

    object Clock : WidgetState() {
        override val clockVisibility: Int = View.VISIBLE
        override val exchangeVisibility: Int = View.GONE
        override val calendarVisibility: Int  = View.GONE
    }

    object Exchange : WidgetState() {
        override val clockVisibility: Int = View.VISIBLE
        override val exchangeVisibility: Int = View.VISIBLE
        override val calendarVisibility: Int  = View.GONE
    }

    object Calendar : WidgetState() {
        override val clockVisibility: Int = View.VISIBLE
        override val exchangeVisibility: Int = View.GONE
        override val calendarVisibility: Int  = View.VISIBLE
    }
}

fun resolveVisibility(state: WidgetState, remoteViews: RemoteViews) {
    remoteViews.setViewVisibility(R.id.clockContainer, state.clockVisibility)
    remoteViews.setViewVisibility(R.id.exchangeContainer, state.exchangeVisibility)
    remoteViews.setViewVisibility(R.id.calendarSelector, state.calendarVisibility)
    remoteViews.setViewVisibility(R.id.calendarContainer, state.calendarVisibility)
}

fun rescale(newOptions: Bundle, context: Context, remoteViews: RemoteViews) {
    val maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH) / 2

    val pxSize = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        maxWidth.toFloat(),
        context.resources.displayMetrics
    ).toInt()
    val newSizeTime: Float = refitText("04:20", pxSize)
    val newSizeDate: Float = refitText("31.12.2022", pxSize)

//    remoteViews.setTextViewTextSize(R.id.timeText, TypedValue.COMPLEX_UNIT_PX, newSizeTime)
    println("MILIWIDGET autoscale time")
//    remoteViews.setTextViewTextSize(R.id.dateText, TypedValue.COMPLEX_UNIT_PX, newSizeDate)
    println("MILIWIDGET autoscale date")
}

private fun refitText(text: String, textWidth: Int): Float {
    if (textWidth <= 0) return 0f
    var hi = 100f
    var lo = 2f
    val threshold = 0.5f // How close we have to be
    val testPaint = Paint()
    while (hi - lo > threshold) {
        val size = (hi + lo) / 2
        testPaint.textSize = size
        if (testPaint.measureText(text) >= textWidth)
            hi = size // too big
        else
            lo = size // too small
    }
    // Use lo so that we undershoot rather than overshoot
    println("MILIWIDGET autoscale $lo")
    return lo
}



