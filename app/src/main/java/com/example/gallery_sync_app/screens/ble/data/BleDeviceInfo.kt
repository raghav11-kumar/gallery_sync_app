package com.example.gallery_sync_app.screens.ble.data

import android.bluetooth.BluetoothDevice

data class BleDeviceInfo(
    val device: BluetoothDevice,
    val deviceName: String,
    val macAddress: String,
    val rssiText: Int
)
