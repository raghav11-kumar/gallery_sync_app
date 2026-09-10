package com.example.gallery_sync_app.screens.ble

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.gallery_sync_app.screens.ble.data.BleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class BluetoothService(val context: Context) {
    private val _writeSuccess = MutableStateFlow<Boolean>(false)
    val writeSuccess = _writeSuccess.asStateFlow()

    private val _notificationData = MutableStateFlow<BleResponse?>(null)
    val notificationData = _notificationData.asStateFlow()

    fun setWriteSuccess(success: Boolean) {
        _writeSuccess.value = success
    }

    private var bluetoothGatt: BluetoothGatt? = null

    private val serviceUuid = UUID.fromString("d43e0800-d5a5-d3e5-b13e-3922431410be")
    private val characteristicUuid = UUID.fromString("d43e0811-d5a5-d3e5-b13e-3922431410be")
    private val meteringUUID = UUID.fromString("d43e0822-d5a5-d3e5-b13e-3922431410be")
    private val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    fun connect(device: BluetoothDevice) {
        try {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothGatt = device.connectGatt(
                    context, false, gattCallback
                )
                Log.e(
                    "BLE",
                    "the gattSever Of The Device ${device}->Services ${bluetoothGatt?.services ?: "No Services"}"
                )
            } else {
                Log.e("Ble", "Need Bluetooth Connect Permission To Connect To That Device")

            }
        } catch (e: Exception) {
            Log.e("BLE", "Failed To Connect Cuz :${e.message}")
        }
    }


    private val gattCallback = object : BluetoothGattCallback() {
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            //dis
            //con


        }



        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

        }

    }


    fun disConnect() {
        if (ContextCompat.checkSelfPermission(

                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothGatt?.disconnect()
            bluetoothGatt?.close()
            bluetoothGatt = null
            setWriteSuccess(false)
            _notificationData.value = null

        }

    }
}