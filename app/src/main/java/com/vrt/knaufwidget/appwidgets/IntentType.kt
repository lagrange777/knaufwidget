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

    object FXExc : IntentType() {
        const val ACTION_FX_EXC = "FX_EXC"
        const val FX_EXC_INFO_KEY = "FX_EXC_INFO_KEY"
    }

    object FXEr : IntentType() {
        const val ACTION_FX_ER = "FX_ER"
        const val FX_ER_INFO_KEY = "FX_ER_INFO_KEY"
    }

    object OpenCalendar : IntentType() {
        const val ACTION_OPEN_NATIVE_CALENDAR = "ACTION_OPEN_NATIVE_CALENDAR"
    }

    object OpenClock : IntentType() {
        const val ACTION_OPEN_CLOCK = "ACTION_OPEN_CLOCK"
    }

    object OpenFX : IntentType() {
        const val ACTION_OPEN_FX = "ACTION_OPEN_FX"
    }

    object ChangeColorSchema: IntentType() {
        const val ACTION_CHANGE_COLOR_SCHEMA = "ACTION_CHANGE_COLOR_SCHEMA"
    }
}