package com.vrt.knaufwidget

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject

object JsonHelper {
    val json = Json { ignoreUnknownKeys = true }
}

@ExperimentalSerializationApi
fun JSONMessage.toJSONObject(): JSONObject {
    val string = JsonHelper.json.encodeToString(this)
    return JSONObject(string)
}

@ExperimentalSerializationApi
inline fun <reified T> String.toClassObject() : T {
    return JsonHelper.json.decodeFromString(this)
}

@ExperimentalSerializationApi
inline fun <reified T> JSONObject.toClassObject() : T {
    return this.toString().toClassObject()
}

fun JSONObject.getMsgID() = (this.optString("msgid") ?: "-1").toInt()