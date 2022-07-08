package com.vrt.knaufwidget

import android.content.Context
import java.util.*

class KnaufMonth(context: Context, private val calendar: Calendar = Calendar.getInstance()) {

    companion object {
        val cellList: List<Int> = listOf(
            R.id.textView50,
            R.id.textView51,
            R.id.textView52,
            R.id.textView53,
            R.id.textView54,
            R.id.textView55,
            R.id.textView56,
            R.id.textView58,
            R.id.textView59,
            R.id.textView60,
            R.id.textView61,
            R.id.textView62,
            R.id.textView63,
            R.id.textView64,
            R.id.textView66,
            R.id.textView67,
            R.id.textView68,
            R.id.textView69,
            R.id.textView70,
            R.id.textView71,
            R.id.textView72,
            R.id.textView74,
            R.id.textView75,
            R.id.textView76,
            R.id.textView77,
            R.id.textView78,
            R.id.textView79,
            R.id.textView80,
            R.id.textView82,
            R.id.textView83,
            R.id.textView84,
            R.id.textView85,
            R.id.textView86,
            R.id.textView87,
            R.id.textView88,
            R.id.textView90,
            R.id.textView91,
            R.id.textView92,
            R.id.textView93,
            R.id.textView94,
            R.id.textView95,
            R.id.textView96
        )

        val weeksCellsList: List<Int> = listOf(
            R.id.textView49,
            R.id.textView57,
            R.id.textView65,
            R.id.textView73,
            R.id.textView81,
            R.id.textView89
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

    val days: MutableList<KnaufDay>
        get() = getAllDays()

    val weeksRange: List<Int>
        get() = run {
            val res = mutableListOf<Int>()
            val c = Calendar.getInstance()
            c.set(Calendar.DAY_OF_MONTH, daysInMonth)
            val endRange = c.get(Calendar.WEEK_OF_YEAR)
            c.set(Calendar.DAY_OF_MONTH, 1)
            val startRange = c.get(Calendar.WEEK_OF_YEAR)
            (startRange..endRange).forEach { i ->  res.add(i) }
            res
        }

    init {
        val saved = SettingsHelper.getSavedCalendarState(context)
        calendar.set(Calendar.MONTH, saved.first)
        calendar.set(Calendar.YEAR, saved.second)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
    }

    private val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    private val firstDay = calendar.get(Calendar.DAY_OF_WEEK)

    private fun getAllDays(): MutableList<KnaufDay> {
        val days = mutableListOf<KnaufDay>()
        days.addAll(getOutOfRangeDays())
        (1..daysInMonth).forEach { index ->
            val kDay = KnaufDay(index)
            kDay.cellID = cellList[days.count()]
            days.add(kDay)
        }
        return days
    }

    private fun getOutOfRangeDays(): List<KnaufDay> {
        val days = mutableListOf<KnaufDay>()
        var rangeEnd = firstDay - 2
        if (rangeEnd < 0)
            rangeEnd += 7
        (0 until rangeEnd).forEach { index ->
            val kDay = KnaufDay(0)
            kDay.cellID = cellList[index]
            days.add(kDay)
        }
        return days
    }
}

data class KnaufDay(val number: Int) {
    var cellID: Int = 0
    val title: String
        get() = if (number == 0) "" else number.toString()

}


