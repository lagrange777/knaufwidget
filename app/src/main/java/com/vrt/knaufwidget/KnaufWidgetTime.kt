package com.vrt.knaufwidget


import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import androidx.core.os.bundleOf
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class KnaufWidgetTime : AppWidgetProvider() {

    companion object {
        const val FX_URI = """https://cbu.uz/ru/arkhiv-kursov-valyut/json"""
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        println("ONUPDATE 1")
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        context ?: return
        appWidgetManager ?: return
        appWidgetIds ?: return

        appWidgetIds.forEach { appWidgetId ->
            println("ONUPDATE 2")

            val pendingPrevClickIntent = buildIntent(IntentType.PrevClick, appWidgetId, context, javaClass)
            val pendingNextClickIntent = buildIntent(IntentType.NextClick, appWidgetId, context, javaClass)

            val views = RemoteViews(context.packageName, R.layout.knauf_widget_time)
            val w = appWidgetManager.getAppWidgetOptions(appWidgetId)
                .getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
            val h = appWidgetManager.getAppWidgetOptions(appWidgetId)
                .getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)
            scaleText(views, w, getCurDateForClock())
            resolveVisibility(h, views)

            views.setOnClickPendingIntent(R.id.prevMonthBtn, pendingPrevClickIntent)
            views.setOnClickPendingIntent(R.id.nextMonthBtn, pendingNextClickIntent)

            views.setImageViewResource(R.id.imageView2, R.drawable.ic_vector)
            views.setImageViewResource(R.id.prevMonthBtn, R.drawable.ic_arrow_back)
            views.setImageViewResource(R.id.nextMonthBtn, R.drawable.ic_arrow_forward)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
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

        val remoteViews = RemoteViews(context.packageName, R.layout.knauf_widget_time)

        val maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)
        val maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)

        remoteViews.setImageViewResource(R.id.imageView2, R.drawable.ic_vector)
        resolveVisibility(maxHeight, remoteViews)
        scaleText(remoteViews, maxWidth, getCurDateForClock())

        updateCalendar(context, remoteViews)

        remoteViews.setTextViewCompoundDrawables(R.id.textView49, 0, 0, 0, R.drawable.red_dot)
        remoteViews.setTextViewCompoundDrawables(R.id.textView50, 0, 0, 0, R.drawable.red_dot)
        remoteViews.setTextViewCompoundDrawables(R.id.textView51, 0, 0, 0, R.drawable.red_dot)
        remoteViews.setTextViewCompoundDrawables(R.id.textView52, 0, 0, 0, R.drawable.red_dot)
        remoteViews.setTextViewCompoundDrawables(R.id.textView53, 0, 0, 0, R.drawable.red_dot)

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

        askFX(context, CurrencyType.RUB, appWidgetId, javaClass)
        askFX(context, CurrencyType.USD, appWidgetId, javaClass)
        askFX(context, CurrencyType.EUR, appWidgetId, javaClass)

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: return
        val action = intent?.action ?: return
        val fxInfoString = intent.getStringExtra(IntentType.NewFX.FX_INFO_KEY) ?: return
        val fxInfo : FXEntity = JSONObject(fxInfoString).toClassObject()
        fillInfo(fxInfo, context)
    }
}

private fun updateCalendar(context: Context, remoteViews: RemoteViews) {
    val helper = KnaufMonth(context)
    var counter = 0
    helper.days.forEach { day ->
        counter++
        remoteViews.setTextViewText(day.cellID, day.title)
    }
    (counter until KnaufMonth.cellList.count()).forEach { index ->
        remoteViews.setViewVisibility(KnaufMonth.cellList[index], View.GONE)
    }

    counter = 0
    helper.weeksRange.forEachIndexed { index, i ->
        counter++
        val cellID = KnaufMonth.weeksCellsList[index]
        val text = i.toString()
        remoteViews.setTextViewText(cellID, text)
    }

    (counter until KnaufMonth.weeksCellsList.count()).forEach { index ->
        remoteViews.setViewVisibility(KnaufMonth.weeksCellsList[index], View.GONE)
    }
}

private fun fillInfo(fxInfo: FXEntity, context: Context) {
    when (fxInfo.curName) {
        CurrencyType.RUB.curName -> fillFXRow(context, R.id.rubRateText to R.id.rubDiffText, fxInfo)
        CurrencyType.USD.curName -> fillFXRow(context, R.id.usdRateText to R.id.usdDiffText, fxInfo)
        CurrencyType.EUR.curName -> fillFXRow(context, R.id.eurRateText to R.id.eurDiffText, fxInfo)
    }
}

private fun fillFXRow(context: Context, cells: Pair<Int, Int>, fxInfo: FXEntity) {
    val remoteViews = RemoteViews(context.packageName, R.layout.knauf_widget_time)
    remoteViews.setTextViewText(cells.first, fxInfo.rate)
    remoteViews.setTextViewText(cells.second, fxInfo.diff)
    AppWidgetManager.getInstance(context).updateAppWidget(fxInfo.widgetID, remoteViews)
}

sealed class WidgetState {
    abstract val clockVisibility: Int
    abstract val exchangeVisibility: Int
    abstract val calendarVisibility: Int

    object Clock : WidgetState() {
        override val clockVisibility: Int = View.VISIBLE
        override val exchangeVisibility: Int = View.GONE
        override val calendarVisibility: Int = View.GONE
    }

    object Exchange : WidgetState() {
        override val clockVisibility: Int = View.VISIBLE
        override val exchangeVisibility: Int = View.VISIBLE
        override val calendarVisibility: Int = View.GONE
    }

    object Calendar : WidgetState() {
        override val clockVisibility: Int = View.VISIBLE
        override val exchangeVisibility: Int = View.GONE
        override val calendarVisibility: Int = View.VISIBLE
    }
}


fun resolveVisibility(h: Int, remoteViews: RemoteViews) {
    val state =
        when {
            h < 200 -> WidgetState.Clock
            h in 200..300 -> WidgetState.Exchange
            else -> WidgetState.Calendar
        }
    remoteViews.setViewVisibility(R.id.clockContainer, state.clockVisibility)
    remoteViews.setViewVisibility(R.id.exchangeContainer, state.exchangeVisibility)
    remoteViews.setViewVisibility(R.id.calendarSelector, state.calendarVisibility)
    remoteViews.setViewVisibility(R.id.calendarContainer, state.calendarVisibility)
}

private fun scaleText(remoteViews: RemoteViews, maxWidth: Int, time: Pair<String, String>) {
    val newSizeTime = calc(time.first, maxWidth / 2 - 16)
    val newSizeDate = calc(time.second, maxWidth / 2 - 16)
    remoteViews.setTextViewTextSize(R.id.timeText, TypedValue.COMPLEX_UNIT_PX, newSizeTime)
    remoteViews.setTextViewTextSize(R.id.dateText, TypedValue.COMPLEX_UNIT_PX, newSizeDate)
//    remoteViews.setTextViewText(R.id.timeText, time.first)
//    remoteViews.setTextViewText(R.id.dateText, time.second)
}

private fun calc(text: String, maxWidth: Int): Float {
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

private fun getCurDateForClock(): Pair<String, String> {
    val t = Date()
    val tf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return tf.format(t) to df.format(t)
}

private fun getCurDateForFX(): String {
    val t = Date()
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return df.format(t)
}


sealed class IntentType {

    object PrevClick : IntentType() {
        const val ACTION_PREV_MONTH_CLICK = "PREV_MONTH"
    }

    object NextClick : IntentType() {
        const val ACTION_NEXT_MONTH_CLICK = "NEXT_MONTH"
    }

    object NewFX : IntentType() {
        const val ACTION_NEW_FX = "NEW_FX"
        const val FX_INFO_KEY = "FX_INFO_KEY"
    }
}

sealed class CurrencyType {
    abstract val curName: String
    object RUB : CurrencyType() {
        override val curName: String = "RUB"
    }

    object USD : CurrencyType() {
        override val curName: String = "USD"
    }

    object EUR : CurrencyType() {
        override val curName: String = "EUR"
    }

    object GBP : CurrencyType() {
        override val curName: String = "GBP"
    }
}

private fun <T> buildIntent(
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
        }
        IntentType.NextClick -> {
            intent.action = IntentType.NextClick.ACTION_NEXT_MONTH_CLICK
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        IntentType.NewFX -> {
            intent.action = IntentType.NewFX.ACTION_NEW_FX
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            infoString?.let { info -> intent.putExtra(IntentType.NewFX.FX_INFO_KEY, info) }
        }
    }
    return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

private fun <T> askFX(
    context: Context,
    currency: CurrencyType,
    appWidgetId: Int,
    javaClass: Class<T>
) {

    val fullURL = "${KnaufWidgetTime.FX_URI}/${currency.curName}/${getCurDateForFX()}/"
    val request = JsonArrayRequest(
        Request.Method.GET, fullURL, null,
        { response ->
            try {
                val res = response.getJSONObject(0)
                val fxAnswer = res.toClassObject<FXAnswer>()
                val info = FXEntity(fxAnswer.symCode, fxAnswer.rate, fxAnswer.diff, appWidgetId).toJSONObject().toString()
                buildIntent(IntentType.NewFX, appWidgetId, context, javaClass, info).send()
            } catch (e: Exception) {
                Log.d("FXTEST VOLLEY EXC", "$e")
            }

        }, { error ->
            Log.d("FXTEST VOLLEY ERROR", "$error")
        })
    request.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10, 0, 1f)
    request.setShouldCache(false)
    VolleySingleton.getInstance(context).addToRequestQueue(request)
}




