package com.example.gallery_sync_app.screens.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.gallery_sync_app.screens.ble.data.BleResponse
import com.example.gallery_sync_app.screens.ble.data.BleWrite
import com.example.gallery_sync_app.screens.ble.data.DeviceInfo
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import com.example.gallery_sync_app.screens.websockets.WebSocketResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

class BluetoothService(val context: Context) {
    private val writeSuccess = MutableStateFlow(false)
    val writeSuccessInfo = writeSuccess.asStateFlow()


    private val notificationData = MutableStateFlow<BleResponse?>(null)
    val notificationDataInfo = notificationData.asStateFlow()
    private val deviceInfo = MutableStateFlow<DeviceInfo?>(null)
    val deviceInformation = deviceInfo.asStateFlow()
    private fun setDeviceInfo(deviceInfo: DeviceInfo) {
        this.deviceInfo.value = deviceInfo
    }


    private fun setWriteSuccess(success: Boolean) {
        writeSuccess.value = success
    }

    private var bluetoothGatt: BluetoothGatt? = null

    private val serviceUuid = UUID.fromString("d43e0800-d5a5-d3e5-b13e-3922431410be")
    private val meteringUUID = UUID.fromString("d43e0822-d5a5-d3e5-b13e-3922431410be")
    private val characteristicUUId = UUID.fromString("d43e0811-d5a5-d3e5-b13e-3922431410be")
    private val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    fun bondDevice(device: BluetoothDevice) {

        if (!ReusableFunctions.checkPermission(context)) {
            Log.e(
                "BLE", "BLUETOOTH_CONNECT permission required"
            )
            return
        }


        when (device.bondState) {

            BluetoothDevice.BOND_NONE -> {

                Log.d(
                    "BLESERVICE", "Device not bonded. Starting bonding..."
                )

                val started = device.createBond()

                Log.d(
                    "BLESERVICE", "createBond() returned: $started"
                )
            }

            BluetoothDevice.BOND_BONDING -> {

                Log.d(
                    "BLESERVICE", "Bonding already in progress"
                )
            }

            BluetoothDevice.BOND_BONDED -> {

                Log.d(
                    "BLESERVICE", "Device already bonded. Connecting..."
                )

            }
        }
    }

    private var targetDevice: BluetoothDevice? = null
    private var isConnected = MutableStateFlow(false)
    val isConnectedInfo = isConnected.asStateFlow()


    fun connect(device: BluetoothDevice) {
        try {
            setDeviceInfo(
                DeviceInfo(
                    device = device,
                    deviceName = device.name,
                    macAddress = device.address,
                    charUUID = characteristicUUId.toString(),
                    serviceUUID = serviceUuid.toString(),
                    bondState = device.bondState.toString(),
                    connected = device.bondState.toString()

                )
            )

            if (ReusableFunctions.checkPermission(context)) {
                bluetoothGatt = device.connectGatt(
                    context, false, gattCallBack
                )
                Log.e(
                    "BLESERVICE", "Connecting to Gatt Server: ${device.address}"
                )
            } else {
                Log.e("BLESERVICE", "Need Bluetooth Connect Permission To Connect To That Device")

            }
        } catch (e: Exception) {
            Log.e("BLESERVICE", "Failed To Connect Cuz :${e.message}")
        }
    }

    private val gattCallBack = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                val device = gatt?.device
                Log.e("BLESERVICE", "SUCCESSFULLY CONNECTED To ${device?.name}")

                isConnected.value = true
                if (ReusableFunctions.checkPermission(context = context)) {
                    gatt?.requestMtu(512)
                }
            }
            if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                setWriteSuccess(false)
                Log.e("BLESERVICE", "Disconnected${status}")
            }

        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
            if (ReusableFunctions.checkPermission(context)) {
                gatt?.discoverServices()
            }
        }

        private val responseBuffer = StringBuilder()

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray
        ) {
            if (characteristic.uuid != meteringUUID) {
                return
            }

            // Convert ONLY this notification packet to String
            val chunk = value.toString(Charsets.UTF_8)

            Log.d(
                "BLESERVICE", "CHUNK (${value.size} bytes): $chunk"
            )
            //complete notification packet
            responseBuffer.append(chunk)
            val fullJson = responseBuffer.toString()

            val startIndex = responseBuffer.indexOf("{")
            val endIndex = responseBuffer.lastIndexOf("}")

            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {

                val fullJson = responseBuffer.substring(startIndex, endIndex + 1)

                Log.d("BLESERVICE", "CLEAN JSON = [$fullJson]")

                try {
                    val response = Gson().fromJson(
                        fullJson, BleResponse::class.java
                    )

                    Log.d("BLESERVICE", "Parsed response = $response")

                    notificationData.value = response

                } catch (e: Exception) {
                    Log.e("BLESERVICE", "Gson parsing failed", e)
                }

                responseBuffer.delete(0, endIndex + 1)
            }


        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.e("BLESERVICE", "NO SERVICE Discovered")
                return
            }
            val service = gatt?.getService(serviceUuid)
            if (service == null) {
                Log.e("BLESERVICE", "No Service Found")
                return
            }
            writeBlaze(gatt)

        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (ReusableFunctions.checkPermission(context)) {
                    val service = gatt?.getService(serviceUuid)
                    if (service == null) {
                        Log.e("BLESERVICE", "No Service Found")
                        return
                    }
                    val meteringCharacteristic = service.getCharacteristic(meteringUUID)

                    Log.e("BLESERVICE", "Descriptor Write Success")
                    val data = BleWrite(
                        "insta_metering_check", "read"
                    )
                    val json = Gson().toJson(data)
                    val bytes = json.toByteArray(Charsets.UTF_8)

                    meteringCharacteristic.writeType =
                        BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                    meteringCharacteristic.value = bytes
                    gatt.writeCharacteristic(meteringCharacteristic)

                }
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (!ReusableFunctions.checkPermission(context = context)) {
                    return
                }
                Log.e(
                    "BLESERVICE", "Write successful: ${characteristic?.uuid}"
                )

                if (characteristic?.uuid == characteristicUUId) {


                    Log.e("BLESERVICE", "Blaze characteristic written successfully")
                    val service = gatt?.getService(serviceUuid)
                    if (service == null) {
                        Log.e("BLESERVICE", "No Service Found")
                        return
                    }
                    if (ReusableFunctions.checkPermission(context)) {
                        val meteringCharacteristic = service.getCharacteristic(meteringUUID)
                        val notificationEnabled = gatt.setCharacteristicNotification(
                            meteringCharacteristic, true
                        )

                        Log.e(
                            "BLESERVICE",
                            "meteringCharacteristic Notification Enabled = $notificationEnabled"
                        )
                        val descriptor = meteringCharacteristic.getDescriptor(cccdUuid)
                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(descriptor)

                    }


                } else if (characteristic?.uuid == meteringUUID) {
                    Log.e("BLESERVICE", "Metering characteristic written successfully")
                    setWriteSuccess(true)
                } else {
                    setWriteSuccess(false)

                }
            }
        }

    }


    private fun writeBlaze(gatt: BluetoothGatt) {
        val service = gatt.getService(serviceUuid)

        if (service == null) {
            Log.e("BLESERVICE", "Service not found")
            return
        }
        val blazeCharacteristic = service.getCharacteristic(characteristicUUId)

        if (blazeCharacteristic == null) {
            Log.e("BLESERVICE", "Blaze characteristic not found")
            return
        }
        if (ReusableFunctions.checkPermission(context)) {


            val bytes = "BLAZE".toByteArray(Charsets.UTF_8)

            blazeCharacteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE

            blazeCharacteristic.value = bytes

            gatt.writeCharacteristic(blazeCharacteristic)

            Log.e("BLESERVICE", "Blaze sent bytes ${bytes.contentToString()}")
        }
    }


    fun disConnect() {
        if (ReusableFunctions.checkPermission(context)) {
            bluetoothGatt?.disconnect()
            bluetoothGatt?.close()
            bluetoothGatt = null
            setWriteSuccess(false)
            notificationData.value = null

        }

    }

    fun isDevicePaired(device: BluetoothDevice): Boolean {
        if (ReusableFunctions.checkPermission(context)) {
            if (device.bondState == BluetoothDevice.BOND_BONDED) return true
        }
        return false

    }


}
