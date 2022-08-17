package com.vrt.knaufwidget

import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import java.text.SimpleDateFormat
import java.util.*


data class WidgetCalendarEvent(
    val title: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val msDate: Long,
    val accName: String
)

data class WidgetCalendarEventTest(
    val id: String,
    val accName: String,
    val calName: String,
    val accOwner: String,
)

val queryListTest = listOf(
    CalendarContract.Calendars._ID,
    CalendarContract.Calendars.ACCOUNT_NAME,
    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
    CalendarContract.Calendars.OWNER_ACCOUNT
).toTypedArray()

val queryList = listOf(
    "calendar_id",
    "account_name",
    "title",
    "description",
    "dtstart",
    "dtend",
    "eventLocation"
).toTypedArray()

class UtilityTest {

    private val events = mutableListOf<WidgetCalendarEventTest>()

    fun readCalendarEvent(context: Context): MutableList<WidgetCalendarEventTest> {
        val cursor = context.contentResolver
            .query(
                Uri.parse("content://com.android.calendar/events"),
                queryListTest,
                null,
                null,
                null
            )
        cursor?.moveToFirst()
        while (true) {
            try {
                val id = cursor?.getString(0) ?: ""
                val accName = cursor?.getString(1) ?: ""
                val calName = cursor?.getString(2) ?: ""
                val accOwner = cursor?.getString(3) ?: ""
                events.add(WidgetCalendarEventTest(id, accName, calName, accOwner))
//                nameOfEvent.add(cursor?.getString(1) ?: "");
//                startDates.add(getDate(cursor?.getString(3)?.toLong() ?: 0L))
//                endDates.add(getDate(cursor?.getString(4)?.toLong() ?: 0L))
//                descriptions.add(cursor?.getString(2) ?: "")

                cursor?.moveToNext() ?: break
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }

        }
        return events;
    }

    fun getDate(milliSeconds: Long): Triple<Int, Int, Int> {
        val formatter = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        formatter.format(calendar.time).split("/").apply {
            return Triple(this[0].toInt(), this[1].toInt(), this[2].toInt())
        }
    }
}

class Utility {

    private val events = mutableListOf<WidgetCalendarEvent>()

    fun readCalendarEvent(context: Context): MutableList<WidgetCalendarEvent> {
        CalendarContract.Calendars._ID
        var counter = 0
        val cursor = context.contentResolver
            .query(
                Uri.parse("content://com.android.calendar/events"),
                queryList,
                null,
                null,
                null
            )
        cursor?.moveToFirst()
        while (counter < (cursor?.count ?: 0)) {
            counter++
            try {
                val date = getDate(cursor?.getString(4)?.toLong() ?: 0L)
                events.add(
                    WidgetCalendarEvent(
                        title = cursor?.getString(2) ?: "",
                        day = date.first,
                        month = date.second,
                        year = date.third,
                        msDate = cursor?.getString(4)?.toLong() ?: 0L,
                        accName = cursor?.getString(1) ?: ""
                    )
                )
                cursor?.moveToNext() ?: break
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }

        }
        return events;
    }

    private fun getDate(milliSeconds: Long): Triple<Int, Int, Int> {
        val formatter = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        formatter.format(calendar.time).split("/").apply {
            return Triple(this[0].toInt(), this[1].toInt(), this[2].toInt())
        }
    }
}

