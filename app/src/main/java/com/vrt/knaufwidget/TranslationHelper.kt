package com.vrt.knaufwidget


object TranslationHelper {

    val colorSchemaLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.colorSchemaLabel
            else -> Translation.RU.colorSchemaLabel
        }
    }

    val calendarSettingsLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.calendarSettingsLabel
            else -> Translation.RU.calendarSettingsLabel
        }
    }

    val mondayLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.mondayLabel
            else -> Translation.RU.mondayLabel
        }
    }
    val tuesdayLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.tuesdayLabel
            else -> Translation.RU.tuesdayLabel
        }
    }
    val wednesdayLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.wednesdayLabel
            else -> Translation.RU.wednesdayLabel
        }
    }
    val thursdayLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.thursdayLabel
            else -> Translation.RU.thursdayLabel
        }
    }
    val fridayLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.fridayLabel
            else -> Translation.RU.fridayLabel
        }
    }
    val saturdayLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.saturdayLabel
            else -> Translation.RU.saturdayLabel
        }
    }
    val sundayLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.sundayLabel
            else -> Translation.RU.sundayLabel
        }
    }

    val januaryLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.januaryLabel
            else -> Translation.RU.januaryLabel
        }
    }
    val februaryLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.februaryLabel
            else -> Translation.RU.februaryLabel
        }
    }
    val marchLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.marchLabel
            else -> Translation.RU.marchLabel
        }
    }
    val aprilLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.aprilLabel
            else -> Translation.RU.aprilLabel
        }
    }
    val mayLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.mayLabel
            else -> Translation.RU.mayLabel
        }
    }
    val juneLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.juneLabel
            else -> Translation.RU.juneLabel
        }
    }
    val julyLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.julyLabel
            else -> Translation.RU.julyLabel
        }
    }
    val augustLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.augustLabel
            else -> Translation.RU.augustLabel
        }
    }
    val septemberLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.septemberLabel
            else -> Translation.RU.septemberLabel
        }
    }
    val octoberLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.octoberLabel
            else -> Translation.RU.octoberLabel
        }
    }
    val novemberLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.novemberLabel
            else -> Translation.RU.novemberLabel
        }
    }
    val decemberLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.decemberLabel
            else -> Translation.RU.decemberLabel
        }
    }

    val currencyLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.currencyLabel
            else -> Translation.RU.currencyLabel
        }
    }
    val rateLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.rateLabel
            else -> Translation.RU.rateLabel
        }
    }
    val changeLabel: (String) -> String = { langCode ->
        when (langCode) {
            Translation.UZ.langCode -> Translation.UZ.changeLabel
            else -> Translation.RU.changeLabel
        }
    }
}


sealed class Translation {

    abstract val langCode: String

    abstract val colorSchemaLabel: String
    abstract val calendarSettingsLabel: String

    abstract val mondayLabel: String
    abstract val tuesdayLabel: String
    abstract val wednesdayLabel: String
    abstract val thursdayLabel: String
    abstract val fridayLabel: String
    abstract val saturdayLabel: String
    abstract val sundayLabel: String

    abstract val januaryLabel: String
    abstract val februaryLabel: String
    abstract val marchLabel: String
    abstract val aprilLabel: String
    abstract val mayLabel: String
    abstract val juneLabel: String
    abstract val julyLabel: String
    abstract val augustLabel: String
    abstract val septemberLabel: String
    abstract val octoberLabel: String
    abstract val novemberLabel: String
    abstract val decemberLabel: String

    abstract val currencyLabel: String
    abstract val rateLabel: String
    abstract val changeLabel: String

    object RU : Translation() {

        override val langCode: String = "RU"

        override val colorSchemaLabel: String = "Выбрана цветовая схема %"
        override val calendarSettingsLabel: String = "Настройки календаря"

        override val mondayLabel: String = "Пн"
        override val tuesdayLabel: String = "Вт"
        override val wednesdayLabel: String = "Ср"
        override val thursdayLabel: String = "Чт"
        override val fridayLabel: String = "Пт"
        override val saturdayLabel: String = "Сб"
        override val sundayLabel: String = "Вс"

        override val januaryLabel: String = "Январь"
        override val februaryLabel: String = "Февраль"
        override val marchLabel: String = "Март"
        override val aprilLabel: String = "Апрель"
        override val mayLabel: String = "Май"
        override val juneLabel: String = "Июнь"
        override val julyLabel: String = "Июль"
        override val augustLabel: String = "Август"
        override val septemberLabel: String = "Сентябрь"
        override val octoberLabel: String = "Октябрь"
        override val novemberLabel: String = "Ноябрь"
        override val decemberLabel: String = "Декабрь"

        override val currencyLabel: String = "Валюта"
        override val rateLabel: String = "Курс"
        override val changeLabel: String = "Изменение"
    }

    object UZ : Translation() {

        override val langCode: String = "UZ"

        override val colorSchemaLabel: String = "%-rang sxemasi tanlangan"
        override val calendarSettingsLabel: String = "Taqvim sozlamalari"

        override val mondayLabel: String = "Du"
        override val tuesdayLabel: String = "Se"
        override val wednesdayLabel: String = "Ch"
        override val thursdayLabel: String = "Pa"
        override val fridayLabel: String = "Ju"
        override val saturdayLabel: String = "Sh"
        override val sundayLabel: String = "Ya"

        override val januaryLabel: String = "Yanvar"
        override val februaryLabel: String = "Fevral"
        override val marchLabel: String = "Mart"
        override val aprilLabel: String = "Aprel"
        override val mayLabel: String = "May"
        override val juneLabel: String = "Iyun"
        override val julyLabel: String = "Iyul"
        override val augustLabel: String = "Avgust"
        override val septemberLabel: String = "Sentabr"
        override val octoberLabel: String = "Oktabr"
        override val novemberLabel: String = "Noyabr"
        override val decemberLabel: String = "Dekabr"

        override val currencyLabel: String = "Valyuta"
        override val rateLabel: String = "Kurs"
        override val changeLabel: String = "Oʻzgarish"
    }
}