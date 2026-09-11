package com.example.gallery_sync_app.screens.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.util.Log
import com.example.gallery_sync_app.screens.ble.data.BleResponse
import com.example.gallery_sync_app.screens.ble.data.BleWrite
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class BluetoothService(val context: Context) {
    private val _writeSuccess = MutableStateFlow(false)
    val writeSuccess = _writeSuccess.asStateFlow()

    private val _notificationData = MutableStateFlow<BleWrite?>(null)
    val notificationData = _notificationData.asStateFlow()

    fun setWriteSuccess(success: Boolean) {
        _writeSuccess.value = success
    }

    private var bluetoothGatt: BluetoothGatt? = null

    private val serviceUuid = UUID.fromString("d43e0800-d5a5-d3e5-b13e-3922431410be")
    private val meteringUUID = UUID.fromString("d43e0822-d5a5-d3e5-b13e-3922431410be")
    private val characteristicUUId = UUID.fromString("d43e0811-d5a5-d3e5-b13e-3922431410be")
    private val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    fun bondDevice(device: BluetoothDevice) {

        if (!ReusableFunctions.checkPermission(context)) {
            Log.e("BLE", "BLUETOOTH_CONNECT permission required")
            return
        }

        when (device.bondState) {

            BluetoothDevice.BOND_NONE -> {
                val started = device.createBond()

                Log.d(
                    "BLE",
                    "Bonding started = $started"
                )
            }

            BluetoothDevice.BOND_BONDING -> {
                Log.d("BLE", "Bonding already in progress")
            }

            BluetoothDevice.BOND_BONDED -> {
                Log.d("BLE", "Device is already bonded")
            }
        }
    }
    fun connect(device: BluetoothDevice) {
        try {

            if (ReusableFunctions.checkPermission(context)) {
                bluetoothGatt = device.connectGatt(
                    context, false, gattCallBack
                )
                Log.e(
                    "BLE",
                    "Connecting to Gatt Server: ${device.address}"
                )
            } else {
                Log.e("Ble", "Need Bluetooth Connect Permission To Connect To That Device")

            }
        } catch (e: Exception) {
            Log.e("BLE", "Failed To Connect Cuz :${e.message}")
        }
    }
    val responseBuffer = StringBuilder()
private val gattCallBack=object : BluetoothGattCallback(){

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            Log.e("BLESERVICE", "SUCCESSFULLY CONNECTED")
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
        if(ReusableFunctions.checkPermission(context))
        gatt?.discoverServices()
    }
    private val responseBuffer = StringBuilder()

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray
    ) {
        if (characteristic.uuid != meteringUUID) {
            return
        }

        // Convert ONLY this notification packet to String
        val chunk = value.toString(Charsets.UTF_8)

        Log.d(
            "BLE_DATA",
            "CHUNK (${value.size} bytes): $chunk"
        )

        // Add this packet to the complete response
        responseBuffer.append(chunk)

        Log.d(
            "BLE_DATA",
            "BUFFER (${responseBuffer.length} chars): ${String(responseBuffer)})"
        )

        // Try to extract a complete JSON object

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
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        super.onDescriptorWrite(gatt, descriptor, status)
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(ReusableFunctions.checkPermission(context)){
                val service = gatt?.getService(serviceUuid)
                if (service == null) {
                    Log.e("BLESERVICE", "No Service Found")
                    return
                }
                val meteringCharacteristic = service.getCharacteristic(meteringUUID)

                Log.e("BLESERVICE", "Descriptor Write Success")
                val data = BleWrite(
                    "metering_check", "read"
                )
                val json = Gson().toJson(data)
                val bytes = json.toByteArray(Charsets.UTF_8)

                meteringCharacteristic.writeType =
                    BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                meteringCharacteristic.value = bytes
                gatt.writeCharacteristic(meteringCharacteristic)

            }}
    }
    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
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
                if(ReusableFunctions.checkPermission(context)) {
                    val meteringCharacteristic = service.getCharacteristic(meteringUUID)
                    val notificationEnabled = gatt.setCharacteristicNotification(
                        meteringCharacteristic, true
                    )

                    Log.e("BLESERVICE","meteringCharacteristic Notification Enabled = $notificationEnabled")
                    val descriptor =meteringCharacteristic.getDescriptor(cccdUuid)
                    descriptor.value=BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    gatt.writeDescriptor(descriptor)

                }


            }else if(characteristic?.uuid==meteringUUID){
                Log.e("BLESERVICE", "Metering characteristic written successfully")
                setWriteSuccess(true)
            }else{
                setWriteSuccess(false)

            }
        }
    }

}


//    private val gattCallback = object : BluetoothGattCallback() {
//        //this is Where Notify Take Place
//        override fun onCharacteristicChanged(
//            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray
//        ) {
//            super.onCharacteristicChanged(gatt, characteristic, value)
//            if(characteristic.uuid == meteringUUID) {
//                val chunk = String(value, Charsets.UTF_8)
//                Log.d("BLESERVICE", "Chunk received: ${chunk}")
//            }
//
//        }
//
//        //this is Where we get the Response iS the Write Operation was Success Or Not
//        override fun onCharacteristicWrite(
//            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
//        ) {
//            super.onCharacteristicWrite(gatt, characteristic, status)
//
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                if (!ReusableFunctions.checkPermission(context = context)) {
//                    return
//                }
//                Log.e(
//                    "BLESERVICE", "Write successful: ${characteristic?.uuid}"
//                )
//
//                // First characteristic completed
//                if (characteristic?.uuid == characteristicUUId) {
//
//                    Log.e(
//                        "BLESERVICE", "Blaze characteristic written successfully"
//                    )
//                    val service = gatt?.getService(serviceUuid)
//                    if (service == null) {
//                        Log.e("BLESERVICE", "No Service Found")
//                        return
//                    }
//                    val meteringCharacteristic = service.getCharacteristic(meteringUUID)
//
//                    // IMPORTANT: Enable notifications on BOTH Android and Device
//                    enableMeteringNotifications(gatt,meteringCharacteristic)
//
//                    val data = BleWrite(
//                        "metering_check", "read"
//                    )
//                    val json = Gson().toJson(data)
//                    val bytes = json.toByteArray(Charsets.UTF_8)
//
//                    meteringCharacteristic.writeType =
//                        BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
//                    meteringCharacteristic.value = bytes
//
//                    gatt.writeCharacteristic(meteringCharacteristic)
//                }
//
//                // Second characteristic completed
//                else if (characteristic?.uuid == meteringUUID) {
//
//                    Log.e(
//                        "BLESERVICE", "Metering characteristic written successfully"
//                    )
//
//                    setWriteSuccess(true)
//                } else {
//
//                    Log.e(
//                        "BLESERVICE", "Unknown characteristic: ${characteristic?.uuid}"
//                    )
//                }
//
//            } else {
//
//                setWriteSuccess(false)
//
//                Log.e(
//                    "BLESERVICE", "Write failed. UUID=${characteristic?.uuid}, status=$status"
//                )
//            }
//        }
//        //we can check out for the services and characteristics here
//
//        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
//            super.onServicesDiscovered(gatt, status)
//            if (status != BluetoothGatt.GATT_SUCCESS) {
//                Log.e("BLESERVICE", "NO SERVICE Discovered")
//                return
//            }
//            val service = gatt?.getService(serviceUuid)
//            if (service == null) {
//                Log.e("BLESERVICE", "No Service Found")
//                return
//            }
//            writeBlaze(gatt!!)
//        }
//
//
//        //this Tells us if the Connection Was Success Or Not
//
//        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
//            super.onConnectionStateChange(gatt, status, newState)
//            if (newState == BluetoothGatt.STATE_CONNECTED) {
//                Log.e("BLESERVICE", "SUCCESSFULLY CONNECTED")
//                if (ReusableFunctions.checkPermission(context)) {
//                    gatt?.discoverServices()
//                }
//            }
//            if (newState == BluetoothGatt.STATE_DISCONNECTED) {
//                setWriteSuccess(false)
//                Log.e("BLESERVICE", "Disconnected${status}")
//            }
//        }
//    }
//
//
//    private fun enableMeteringNotifications(
//        gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic
//    ) {
//        if (!ReusableFunctions.checkPermission(context = context)) {
//            return
//        }
//
//
//
//        val notificationEnabled = gatt.setCharacteristicNotification(
//            characteristic, true
//        )
//
//        Log.e(
//            "BLE", "setCharacteristicNotification = $notificationEnabled"
//        )
//        val descriptor = characteristic.getDescriptor(cccdUuid)
//
//        if (descriptor == null) {
//            Log.e("BLE", "CCCD descriptor not found")
//            return
//        }
//
//        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
//
//        gatt.writeDescriptor(descriptor)
//    }

    private fun writeBlaze(gatt: BluetoothGatt) {
        val service = gatt.getService(serviceUuid)

        if (service == null) {
            Log.e("BLE", "Service not found")
            return
        }
        val blazeCharacteristic = service.getCharacteristic(characteristicUUId)

        if (blazeCharacteristic == null) {
            Log.e("BLE", "Blaze characteristic not found")
            return
        }
        if (ReusableFunctions.checkPermission(context)) {


            val bytes = "BLAZE".toByteArray(Charsets.UTF_8)

            blazeCharacteristic.writeType =
                BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE

            blazeCharacteristic.value = bytes

            gatt.writeCharacteristic(blazeCharacteristic)

            Log.e("BLE", "Blaze sent bytes ${bytes.contentToString()}")
        }
    }


    fun disConnect() {
        if (ReusableFunctions.checkPermission(context)) {
            bluetoothGatt?.disconnect()
            bluetoothGatt?.close()
            bluetoothGatt = null
            setWriteSuccess(false)
            _notificationData.value = null

        }

    }
    fun isDevicePaired(device: BluetoothDevice): Boolean {
        if (ReusableFunctions.checkPermission(context)) {
            if (device.bondState == BluetoothDevice.BOND_BONDED)
                return true
        }
        return false

    }


}
