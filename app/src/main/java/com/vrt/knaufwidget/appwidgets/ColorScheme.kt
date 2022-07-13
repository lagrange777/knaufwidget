package com.vrt.knaufwidget.appwidgets

import android.content.Context
import com.vrt.knaufwidget.R

sealed class ColorSchema {

    companion object {
        const val SET_BACKGROUND_COLOR_KEY = "setBackgroundColor"
    }

    abstract val schemeID : Int
    abstract val clockText : (Context) -> Int
    abstract val mainBg : (Context) -> Int
    abstract val titleBg : (Context) -> Int
    abstract val subtitleBg : (Context) -> Int
    abstract val contentCalBg : (Context) -> Int
    abstract val contentFXBg : (Context) -> Int
    abstract val weekNumber : (Context) -> Int
    abstract val logo: Int

    object Schema1 : ColorSchema() {
        override val schemeID: Int = 1
        override val clockText : (Context) -> Int = { it.getColor(R.color.schema_1_clock_text)}
        override val mainBg : (Context) -> Int = { it.getColor(R.color.schema_1_main_bg)}
        override val titleBg : (Context) -> Int = { it.getColor(R.color.schema_1_title_bg)}
        override val subtitleBg : (Context) -> Int = { it.getColor(R.color.schema_1_subtitle_bg)}
        override val contentCalBg : (Context) -> Int = { it.getColor(R.color.schema_1_content_cal_bg)}
        override val contentFXBg : (Context) -> Int = { it.getColor(R.color.schema_1_content_fx_bg)}
        override val weekNumber : (Context) -> Int = { it.getColor(R.color.schema_1_week_number)}
        override val logo: Int = R.drawable.ic_main_logo
    }

    object Schema2 : ColorSchema() {
        override val schemeID: Int = 2
        override val clockText : (Context) -> Int = { it.getColor(R.color.schema_2_clock_text)}
        override val mainBg : (Context) -> Int = { it.getColor(R.color.schema_2_main_bg)}
        override val titleBg : (Context) -> Int = { it.getColor(R.color.schema_2_title_bg)}
        override val subtitleBg : (Context) -> Int = { it.getColor(R.color.schema_2_subtitle_bg)}
        override val contentCalBg : (Context) -> Int = { it.getColor(R.color.schema_2_content_cal_bg)}
        override val contentFXBg : (Context) -> Int = { it.getColor(R.color.schema_2_content_fx_bg)}
        override val weekNumber : (Context) -> Int = { it.getColor(R.color.schema_2_week_number)}
        override val logo: Int = R.drawable.ic_main_logo
    }

    object Schema3 : ColorSchema() {
        override val schemeID: Int = 3
        override val clockText : (Context) -> Int = { it.getColor(R.color.schema_3_clock_text)}
        override val mainBg : (Context) -> Int = { it.getColor(R.color.schema_3_main_bg)}
        override val titleBg : (Context) -> Int = { it.getColor(R.color.schema_3_title_bg)}
        override val subtitleBg : (Context) -> Int = { it.getColor(R.color.schema_3_subtitle_bg)}
        override val contentCalBg : (Context) -> Int = { it.getColor(R.color.schema_3_content_cal_bg)}
        override val contentFXBg : (Context) -> Int = { it.getColor(R.color.schema_3_content_fx_bg)}
        override val weekNumber : (Context) -> Int = { it.getColor(R.color.schema_3_week_number)}
        override val logo: Int = R.drawable.ic_main_logo
    }

    object Schema4 : ColorSchema() {
        override val schemeID: Int = 4
        override val clockText : (Context) -> Int = { it.getColor(R.color.schema_4_clock_text)}
        override val mainBg : (Context) -> Int = { it.getColor(R.color.schema_4_main_bg)}
        override val titleBg : (Context) -> Int = { it.getColor(R.color.schema_4_title_bg)}
        override val subtitleBg : (Context) -> Int = { it.getColor(R.color.schema_4_subtitle_bg)}
        override val contentCalBg : (Context) -> Int = { it.getColor(R.color.schema_4_content_cal_bg)}
        override val contentFXBg : (Context) -> Int = { it.getColor(R.color.schema_4_content_fx_bg)}
        override val weekNumber : (Context) -> Int = { it.getColor(R.color.schema_4_week_number)}
        override val logo: Int = R.drawable.ic_white_logo
    }
}
