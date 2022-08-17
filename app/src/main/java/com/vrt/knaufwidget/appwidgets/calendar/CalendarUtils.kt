package com.vrt.knaufwidget.appwidgets.calendar

import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.vrt.knaufwidget.KnaufMonth
import com.vrt.knaufwidget.R

fun updateCalendar(context: Context, widgetID: Int) {
    val remoteViews = RemoteViews(context.packageName, R.layout.knauf_widget_calendar)
    updateCalendar(context, remoteViews, widgetID)
}

fun updateCalendar(context: Context, remoteViews: RemoteViews, widgetID: Int) {
    val helper = KnaufMonth(context)
    var counter = 0

    remoteViews.setTextViewText(R.id.curYearText, helper.curTitle)

    helper.getAllDays {
        it.forEach { day ->
            counter++
            if (day.isMarked)
                remoteViews.setTextViewCompoundDrawables(day.cellID, 0, 0, 0, R.drawable.event_dot)
            else
                remoteViews.setTextViewCompoundDrawables(day.cellID, 0, 0, 0, 0)

            remoteViews.setViewVisibility(day.cellID, View.VISIBLE)
            remoteViews.setTextViewText(day.cellID, day.title)
        }

        (counter until KnaufMonth.cellList.count()).forEach { index ->
            remoteViews.setTextViewCompoundDrawables(KnaufMonth.cellList[index], 0, 0, 0, 0)
            remoteViews.setViewVisibility(KnaufMonth.cellList[index], View.GONE)
        }

        counter = 0
        helper.weeksRange.forEachIndexed { index, i ->
            counter++
            val cellID = KnaufMonth.weeksCellsList[index]
            val text = i.toString()
            remoteViews.setViewVisibility(cellID, View.VISIBLE)
            remoteViews.setTextViewText(cellID, text)
        }

        (counter until KnaufMonth.weeksCellsList.count()).forEach { index ->
            remoteViews.setViewVisibility(KnaufMonth.weeksCellsList[index], View.GONE)
        }
        AppWidgetManager.getInstance(context).updateAppWidget(widgetID, remoteViews)
    }
}