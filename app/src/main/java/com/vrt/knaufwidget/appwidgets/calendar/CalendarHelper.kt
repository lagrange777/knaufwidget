package com.vrt.knaufwidget

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.provider.AlarmClock
import android.provider.CalendarContract
import androidx.core.content.ContextCompat.startActivity
import com.vrt.knaufwidget.appwidgets.SettingsHelper
import com.vrt.knaufwidget.appwidgets.fx.FX_URI
import com.vrt.knaufwidget.appwidgets.fx.FX_URI_INFO
import com.vrt.knaufwidget.outlook.OutlookHelper
import java.util.*


const val TEST_CALENDAR_LINK = """https://calendar.google.com/calendar/u/0?cid=eWFtaXNoYXNoaXNoYUBnbWFpbC5jb20"""

class KnaufMonth(
    private val context: Context,
    private val calendar: Calendar = Calendar.getInstance()
) {

    companion object {
        val cellList: List<Int> = listOf(
            R.id.day1,
            R.id.day2,
            R.id.day3,
            R.id.day4,
            R.id.day5,
            R.id.day6,
            R.id.day7,

            R.id.day8,
            R.id.day9,
            R.id.day10,
            R.id.day11,
            R.id.day12,
            R.id.day13,
            R.id.day14,

            R.id.day15,
            R.id.day16,
            R.id.day17,
            R.id.day18,
            R.id.day19,
            R.id.day20,
            R.id.day21,

            R.id.day22,
            R.id.day23,
            R.id.day24,
            R.id.day25,
            R.id.day26,
            R.id.day27,
            R.id.day28,

            R.id.day29,
            R.id.day30,
            R.id.day31,
            R.id.day32,
            R.id.day33,
            R.id.day34,
            R.id.day35,

            R.id.day36,
            R.id.day37,
            R.id.day38,
            R.id.day39,
            R.id.day40,
            R.id.day41,
            R.id.day42
        )
        val weeksCellsList: List<Int> = listOf(
            R.id.week1,
            R.id.week2,
            R.id.week3,
            R.id.week4,
            R.id.week5,
            R.id.week6
        )
        val monthNames: List<String> = listOf(
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь"
        )

    }

    val curTitle: String
        get() = run {
            val m = calendar.get(Calendar.MONTH).apply { monthNames[this] }
            val y = calendar.get(Calendar.YEAR).toString()
            "${monthNames[m]} $y"
        }

    val weeksRange: List<Int>
        get() = run {
            val res = mutableListOf<Int>()

            calendar.set(Calendar.DAY_OF_MONTH, daysInMonth)
            val endRange = if (calendar.get(Calendar.WEEK_OF_YEAR) == 1) calendar.weeksInWeekYear else calendar.get(Calendar.WEEK_OF_YEAR)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val startRange = calendar.get(Calendar.WEEK_OF_YEAR)
            (startRange..endRange).forEach { i ->  res.add(i) }
            res
        }

    private val daysInMonth
        get() = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    private val firstDay
        get() = calendar.get(Calendar.DAY_OF_WEEK)

    init {
        val saved = SettingsHelper.getSavedCalendarState(context)
        calendar.set(Calendar.MONTH, saved.first)
        calendar.set(Calendar.YEAR, saved.second)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
    }

    fun getAllDays(getAllDays: (MutableList<KnaufDay>) -> Unit ) {
        val days = mutableListOf<KnaufDay>()
        val calendarResolver = Utility()
        val acc = SettingsHelper.getSavedAccForSync(context)
        if (acc != "Outlook") {
            val calDays = calendarResolver.readCalendarEvent(context)

            days.addAll(getOutOfRangeDays())
            (1..daysInMonth).forEach { index ->
                val isMarked =
                    calDays.any {
                        it.day == index && it.month == calendar.get(Calendar.MONTH) + 1 && it.year == calendar.get(Calendar.YEAR)
                    }
                val kDay = KnaufDay(index, isMarked)
                kDay.cellID = cellList[days.count()]
                days.add(kDay)
            }
            getAllDays.invoke(days)
        } else {
            OutlookHelper(context).apply {
                initOutlook(null) {
                    days.addAll(getOutOfRangeDays())
                    (1..daysInMonth).forEach { index ->
                        val isMarked =
                            it.any {
                                it.day == index && it.month == calendar.get(Calendar.MONTH) + 1 && it.year == calendar.get(Calendar.YEAR)
                            }
                        val kDay = KnaufDay(index, isMarked)
                        kDay.cellID = cellList[days.count()]
                        days.add(kDay)
                    }
                    getAllDays.invoke(days)
                }
            }
        }
    }

    private fun getOutOfRangeDays(): List<KnaufDay> {
        val days = mutableListOf<KnaufDay>()
        var rangeEnd = firstDay - 2
        if (rangeEnd < 0)
            rangeEnd += 7
        (0 until rangeEnd).forEach { index ->
            val kDay = KnaufDay(0, false)
            kDay.cellID = cellList[index]
            days.add(kDay)
        }
        return days
    }
}

data class KnaufDay(val number: Int, val isMarked: Boolean) {
    var cellID: Int = 0
    val title: String
        get() = if (number == 0) "" else number.toString()

}

fun startNativeCalendar(context: Context) {
    val builder: Uri.Builder = CalendarContract.CONTENT_URI.buildUpon()
    builder.appendPath("time")
    ContentUris.appendId(builder, Calendar.getInstance().timeInMillis)
    val intent = Intent(Intent.ACTION_VIEW)
        .setData(builder.build())
    intent.flags = FLAG_ACTIVITY_NEW_TASK
    startActivity(context, intent, null)
}

fun startClock(context: Context) {
    val mClockIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
    mClockIntent.flags = FLAG_ACTIVITY_NEW_TASK
    startActivity(context, mClockIntent, null)
}

fun startFX(context: Context) {
    val mFXIntent = Intent(Intent.ACTION_VIEW, Uri.parse(FX_URI_INFO))
    mFXIntent.flags = FLAG_ACTIVITY_NEW_TASK
    startActivity(context, mFXIntent, null)
}

