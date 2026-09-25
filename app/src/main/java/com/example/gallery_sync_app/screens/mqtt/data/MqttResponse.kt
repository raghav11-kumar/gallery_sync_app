package com.example.gallery_sync_app.screens.mqtt.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MqttResponse(
    @SerializedName("eventType", alternate = ["event_type", "event_Type"])
    val eventType: String? = null,

    @SerializedName("eventId", alternate = ["event_id", "event_Id"])
    val eventId: String? = null,

    @SerializedName("timeStamp", alternate = ["timestamp", "time_stamp", "time_Stamp"])
    val timeStamp: String? = null,

    @SerializedName("source")
    val source: String? = null,

    @SerializedName("message", alternate = ["msg", "data"])
    val message: String? = null
)
