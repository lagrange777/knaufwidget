package com.vrt.knaufwidget.appwidgets

sealed class CurrencyType {
    abstract val curName: String
    object RUB : CurrencyType() {
        override val curName: String = "RUB"
    }

    object USD : CurrencyType() {
        override val curName: String = "USD"
    }

    object EUR : CurrencyType() {
        override val curName: String = "EUR"
    }

    object GBP : CurrencyType() {
        override val curName: String = "GBP"
    }
}