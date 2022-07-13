package com.vrt.knaufwidget.appwidgets

import com.vrt.knaufwidget.R

sealed class WidgetState {
    abstract val timeViewID: Int
    abstract val dateViewID: Int

    object Clock : WidgetState() {
        override val timeViewID: Int = R.id.timeTextCl
        override val dateViewID: Int = R.id.dateTextCl
    }

    object FX : WidgetState() {
        override val timeViewID: Int = R.id.timeTextFX
        override val dateViewID: Int = R.id.dateTextFX
    }

    object Calendar : WidgetState() {
        override val timeViewID: Int = R.id.timeTextCal
        override val dateViewID: Int = R.id.dateTextCal
    }
}
