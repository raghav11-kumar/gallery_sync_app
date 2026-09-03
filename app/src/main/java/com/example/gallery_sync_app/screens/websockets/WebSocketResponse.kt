package com.example.gallery_sync_app.screens.websockets

data class WebSocketResponse(
    val v: String,
    val I: String,
    val P: String,
    val S: String,
    val Q: String,
    val device_state: String,
    val event_type: String,
    val Pt: String,
    val St: String,
    val Qt: String,
    val msgCountMin: Int,
    val d_ts: String
)
