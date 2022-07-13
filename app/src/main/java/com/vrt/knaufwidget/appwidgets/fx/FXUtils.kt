package com.vrt.knaufwidget.appwidgets.fx

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.vrt.knaufwidget.*
import com.vrt.knaufwidget.appwidgets.CurrencyType
import com.vrt.knaufwidget.appwidgets.IntentType
import com.vrt.knaufwidget.appwidgets.buildIntent
import java.text.SimpleDateFormat
import java.util.*



const val FX_URI = """https://cbu.uz/ru/arkhiv-kursov-valyut/json"""


fun getCurDateForFX(): String {
    val t = Date()
    val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return df.format(t)
}

fun fillInfo(fxInfo: FXEntity, context: Context) {
    when (fxInfo.curName) {
        CurrencyType.RUB.curName -> fillFXRow(context, R.id.rubRateText to R.id.rubDiffText, fxInfo)
        CurrencyType.USD.curName -> fillFXRow(context, R.id.usdRateText to R.id.usdDiffText, fxInfo)
        CurrencyType.EUR.curName -> fillFXRow(context, R.id.eurRateText to R.id.eurDiffText, fxInfo)
    }
}

private fun fillFXRow(context: Context, cells: Pair<Int, Int>, fxInfo: FXEntity) {
    val remoteViews = RemoteViews(context.packageName, R.layout.knauf_widget_fx)
    remoteViews.setTextViewText(cells.first, fxInfo.rate)
    remoteViews.setTextViewText(cells.second, fxInfo.diff)
    AppWidgetManager.getInstance(context).updateAppWidget(fxInfo.widgetID, remoteViews)
}

fun <T> askFX(
    context: Context,
    currency: CurrencyType,
    appWidgetId: Int,
    javaClass: Class<T>
) {

    val fullURL = "${FX_URI}/${currency.curName}/${getCurDateForFX()}/"
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