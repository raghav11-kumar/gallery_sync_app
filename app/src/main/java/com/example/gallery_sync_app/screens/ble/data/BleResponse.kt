package com.example.gallery_sync_app.screens.ble.data

data class BleResponse(
    val V: List<Double>,
    val I: List<Double>,
    val P: List<Double>,
    val S: List<Double>,
    val Q: List<Double>,
    val Fq: List<Double>
)
