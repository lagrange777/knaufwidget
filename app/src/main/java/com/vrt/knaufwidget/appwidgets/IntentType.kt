package com.vrt.knaufwidget.appwidgets

sealed class IntentType {

    object PrevClick : IntentType() {
        const val ACTION_PREV_MONTH_CLICK = "PREV_MONTH"
        const val WIDGET_INFO_ID = "WIDGET_INFO_ID"
    }

    object NextClick : IntentType() {
        const val ACTION_NEXT_MONTH_CLICK = "NEXT_MONTH"
        const val WIDGET_INFO_ID = "WIDGET_INFO_ID"
    }

    object NewFX : IntentType() {
        const val ACTION_NEW_FX = "NEW_FX"
        const val FX_INFO_KEY = "FX_INFO_KEY"
    }

    object OpenCalendar : IntentType() {
        const val ACTION_OPEN_NATIVE_CALENDAR = "ACTION_OPEN_NATIVE_CALENDAR"
    }

    object OpenClock : IntentType() {
        const val ACTION_OPEN_CLOCK = "ACTION_OPEN_CLOCK"
    }

    object ChangeColorSchema: IntentType() {
        const val ACTION_CHANGE_COLOR_SCHEMA = "ACTION_CHANGE_COLOR_SCHEMA"
    }
}