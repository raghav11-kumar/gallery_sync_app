package com.example.gallery_sync_app.screens.ble.data

import android.bluetooth.BluetoothDevice

data class DeviceInfo(
    val device: BluetoothDevice,
    val deviceName: String,
    val macAddress: String,
    val charUUID: String,
    val serviceUUID: String,
    val bondState: String,
    val connected: String
)
