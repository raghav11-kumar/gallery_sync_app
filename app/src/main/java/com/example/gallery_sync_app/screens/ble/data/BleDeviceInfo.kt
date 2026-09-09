package com.example.gallery_sync_app.screens.ble.data

data class BleDeviceInfo(
    val deviceName: String,
    val macAddress: String,
    val rssiText: Int
)
