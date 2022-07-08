package com.vrt.knaufwidget

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class JSONMessage

@Serializable
sealed class InJSON : JSONMessage()

@Serializable
sealed class OutJSON : JSONMessage()


/**
 *"id": 1,
"Code": "643",
"Ccy": "RUB",
"CcyNm_RU": "Российский рубль",
"CcyNm_UZ": "Rossiya rubli",
"CcyNm_UZC": "Россия рубли",
"CcyNm_EN": "Russian Ruble",
"Nominal": "1",
"Rate": "172.46",
"Diff": "3.62",
"Date": "08.07.2022"
 *
 */

@Serializable
data class FXAnswer(
    val id: Int,
    @SerialName("Code")
    val code: String,
    @SerialName("Ccy")
    val symCode: String,
    @SerialName("CcyNm_RU")
    val rusName: String,
    @SerialName("CcyNm_UZ")
    val uzlName: String,
    @SerialName("CcyNm_UZC")
    val uzcName: String,
    @SerialName("CcyNm_EN")
    val enName: String,
    @SerialName("Nominal")
    val nominal: String,
    @SerialName("Rate")
    val rate: String,
    @SerialName("Diff")
    val diff: String,
    @SerialName("Date")
    val date: String
)

@Serializable
data class FXEntity (
    val curName: String,
    val rate: String,
    val diff: String,
    val widgetID: Int
): JSONMessage()