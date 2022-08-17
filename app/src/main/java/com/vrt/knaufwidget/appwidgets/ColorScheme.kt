package com.vrt.knaufwidget.appwidgets

import android.content.Context
import com.vrt.knaufwidget.R

sealed class ColorSchemaOld {

    companion object {
        const val SET_BACKGROUND_COLOR_KEY = "setBackgroundColor"
    }

    abstract val schemeID: Int
    abstract val clockText: (Context) -> Int
    abstract val mainBg: (Context) -> Int
    abstract val titleBg: (Context) -> Int
    abstract val subtitleBg: (Context) -> Int
    abstract val contentCalBg: (Context) -> Int
    abstract val contentFXBg: (Context) -> Int
    abstract val weekNumber: (Context) -> Int
    abstract val logo: Int
    abstract val bell: Int

    object Schema1 : ColorSchemaOld() {
        override val schemeID: Int = 11
        override val clockText: (Context) -> Int = { it.getColor(R.color.schema_1_clock_text) }
        override val mainBg: (Context) -> Int = { it.getColor(R.color.schema_1_main_bg) }
        override val titleBg: (Context) -> Int = { it.getColor(R.color.schema_1_title_bg) }
        override val subtitleBg: (Context) -> Int = { it.getColor(R.color.schema_1_subtitle_bg) }
        override val contentCalBg: (Context) -> Int = { it.getColor(R.color.schema_1_content_cal_bg) }
        override val contentFXBg: (Context) -> Int = { it.getColor(R.color.schema_1_content_fx_bg) }
        override val weekNumber: (Context) -> Int = { it.getColor(R.color.schema_1_week_number) }
        override val logo: Int = R.drawable.ic_main_logo
        override val bell: Int = R.drawable.ic_bell
    }

    object Schema2 : ColorSchemaOld() {
        override val schemeID: Int = 12
        override val clockText: (Context) -> Int = { it.getColor(R.color.schema_2_clock_text) }
        override val mainBg: (Context) -> Int = { it.getColor(R.color.schema_2_main_bg) }
        override val titleBg: (Context) -> Int = { it.getColor(R.color.schema_2_title_bg) }
        override val subtitleBg: (Context) -> Int = { it.getColor(R.color.schema_2_subtitle_bg) }
        override val contentCalBg: (Context) -> Int = { it.getColor(R.color.schema_2_content_cal_bg) }
        override val contentFXBg: (Context) -> Int = { it.getColor(R.color.schema_2_content_fx_bg) }
        override val weekNumber: (Context) -> Int = { it.getColor(R.color.schema_2_week_number) }
        override val logo: Int = R.drawable.ic_main_logo
        override val bell: Int = R.drawable.ic_bell
    }

    object Schema3 : ColorSchemaOld() {
        override val schemeID: Int = 13
        override val clockText: (Context) -> Int = { it.getColor(R.color.schema_3_clock_text) }
        override val mainBg: (Context) -> Int = { it.getColor(R.color.schema_3_main_bg) }
        override val titleBg: (Context) -> Int = { it.getColor(R.color.schema_3_title_bg) }
        override val subtitleBg: (Context) -> Int = { it.getColor(R.color.schema_3_subtitle_bg) }
        override val contentCalBg: (Context) -> Int = { it.getColor(R.color.schema_3_content_cal_bg) }
        override val contentFXBg: (Context) -> Int = { it.getColor(R.color.schema_3_content_fx_bg) }
        override val weekNumber: (Context) -> Int = { it.getColor(R.color.schema_3_week_number) }
        override val logo: Int = R.drawable.ic_main_logo
        override val bell: Int = R.drawable.ic_bell
    }

    object Schema4 : ColorSchemaOld() {
        override val schemeID: Int = 14
        override val clockText: (Context) -> Int = { it.getColor(R.color.schema_4_clock_text) }
        override val mainBg: (Context) -> Int = { it.getColor(R.color.schema_4_main_bg) }
        override val titleBg: (Context) -> Int = { it.getColor(R.color.schema_4_title_bg) }
        override val subtitleBg: (Context) -> Int = { it.getColor(R.color.schema_4_subtitle_bg) }
        override val contentCalBg: (Context) -> Int = { it.getColor(R.color.schema_4_content_cal_bg) }
        override val contentFXBg: (Context) -> Int = { it.getColor(R.color.schema_4_content_fx_bg) }
        override val weekNumber: (Context) -> Int = { it.getColor(R.color.schema_4_week_number) }
        override val logo: Int = R.drawable.ic_white_logo
        override val bell: Int = R.drawable.ic_bell
    }
}


sealed class ColorSchemaNew {

    companion object {
        const val SET_BACKGROUND_COLOR_KEY = "setBackgroundColor"
    }

    abstract val schemaName: Int
    abstract val schemeID: Int
    abstract val clockText: (Context) -> Int
    abstract val mainBg: (Context) -> Int

    abstract val titleCalBg: (Context) -> Int
    abstract val subtitleCalBg: (Context) -> Int
    abstract val contentCalBg: (Context) -> Int
    abstract val weekNumber: (Context) -> Int

    abstract val titleFXBg: (Context) -> Int
    abstract val contentFXBg: (Context) -> Int

    abstract val logo: Int
    abstract val bell: Int

    object Schema1 : ColorSchemaNew() {
        override val schemaName: Int = 1
        override val schemeID: Int = 11
        override val clockText: (Context) -> Int = { it.getColor(R.color.schema_5_clock_text) }
        override val mainBg: (Context) -> Int = { it.getColor(R.color.schema_5_main_bg) }

        override val titleCalBg: (Context) -> Int = { it.getColor(R.color.schema_5_title_cal_bg) }
        override val subtitleCalBg: (Context) -> Int = { it.getColor(R.color.schema_5_subtitle_cal_bg) }
        override val contentCalBg: (Context) -> Int = { it.getColor(R.color.schema_5_content_cal_bg) }
        override val weekNumber: (Context) -> Int = { it.getColor(R.color.schema_5_week_number) }

        override val titleFXBg: (Context) -> Int = { it.getColor(R.color.schema_5_title_fx_bg) }
        override val contentFXBg: (Context) -> Int = { it.getColor(R.color.schema_5_content_fx_bg) }

        override val logo: Int = R.drawable.ic_main_logo
        override val bell: Int = R.drawable.ic_bell
    }

    object Schema2 : ColorSchemaNew() {
        override val schemaName: Int = 2
        override val schemeID: Int = 12
        override val clockText: (Context) -> Int = { it.getColor(R.color.schema_6_clock_text) }
        override val mainBg: (Context) -> Int = { it.getColor(R.color.schema_6_main_bg) }

        override val titleCalBg: (Context) -> Int = { it.getColor(R.color.schema_6_title_cal_bg) }
        override val subtitleCalBg: (Context) -> Int = { it.getColor(R.color.schema_6_subtitle_cal_bg) }
        override val contentCalBg: (Context) -> Int = { it.getColor(R.color.schema_6_content_cal_bg) }
        override val weekNumber: (Context) -> Int = { it.getColor(R.color.schema_6_week_number) }

        override val titleFXBg: (Context) -> Int = { it.getColor(R.color.schema_6_title_fx_bg) }
        override val contentFXBg: (Context) -> Int = { it.getColor(R.color.schema_6_content_fx_bg) }

        override val logo: Int = R.drawable.ic_main_logo
        override val bell: Int = R.drawable.ic_bell
    }
}
