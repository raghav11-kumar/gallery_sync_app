package com.example.gallery_sync_app.screens.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.os.Build
import android.util.Log
import com.example.gallery_sync_app.screens.ble.data.BleResponse
import com.example.gallery_sync_app.screens.ble.data.BleWrite
import com.example.gallery_sync_app.screens.ble.data.CombinedMeteringData
import com.example.gallery_sync_app.screens.ble.data.DeviceInfo
import com.example.gallery_sync_app.screens.ble.data.SecBleResponse
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

class BluetoothService(val context: Context) {
    private val writeSuccess = MutableStateFlow(false)
    val writeSuccessInfo = writeSuccess.asStateFlow()


    private val notificationData = MutableStateFlow<BleResponse?>(null)
    private val deviceInfo = MutableStateFlow<DeviceInfo?>(null)
    val deviceInformation = deviceInfo.asStateFlow()
    private val errorFlow = MutableSharedFlow<String>()
    val errorFlowing = errorFlow.asSharedFlow()

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
    private val ccdUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")


    private var isConnected = MutableStateFlow(false)
    val isConnectedInfo = isConnected.asStateFlow()
    private var pollingJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    private var firstMeteringResponse: BleResponse? = null
    private var secondMeteringResponse: SecBleResponse? = null
    private var waitingForSecondResponse = false
    fun checkAndBond(device: BluetoothDevice) {

        if (!ReusableFunctions.checkPermission(context)) {
            return
        }

        when (device.bondState) {

            BluetoothDevice.BOND_BONDED -> {
                Log.d("BluetoothBond", "Device already bonded")
                connect(device)
            }

            BluetoothDevice.BOND_BONDING -> {
                Log.d("BluetoothBond", "Bonding already in progress")
            }

            BluetoothDevice.BOND_NONE -> {

                Log.d(
                    "BluetoothBond", "Device is not bonded. Calling createBond()..."
                )
                val result = if (Build.VERSION.SDK_INT >= 37) {
                    device.createBond(BluetoothDevice.TRANSPORT_LE)
                } else {
                    device.createBond()
                }

                Log.d(
                    "BluetoothBond", "createBond() returned = $result"
                )
            }
        }
    }

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
                    context, false, gattCallBack, BluetoothDevice.TRANSPORT_LE

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
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    val device = gatt?.device
                    Log.e("BLESERVICE", "SUCCESSFULLY CONNECTED To ${device?.name}")

                    isConnected.value = true
                    if (ReusableFunctions.checkPermission(context = context)) {
                        gatt?.requestMtu(512)
                    }
                }
                if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    Log.e("BLESERVICE", "State_DISCONNECTED")
                    pollingJob?.cancel()
                    serviceScope.launch {
                        errorFlow.emit("Bluetooth DisConnected")
                    }
                    setWriteSuccess(false)
                    Log.e("BLESERVICE", "Disconnected${status}")
                }
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

                    if (!waitingForSecondResponse) {

                        // FIRST RESPONSE
                        val response = Gson().fromJson(
                            fullJson,
                            BleResponse::class.java
                        )

                        Log.d(
                            "BLESERVICE",
                            "First metering response = $response"
                        )

                        firstMeteringResponse = response
                        notificationData.value = response


                        // Now we have first response.
                        // Ask device for second response.
                        waitingForSecondResponse = true

                        sendSecondMeteringCommand()

                    } else {

                        // SECOND RESPONSE
                        val response = Gson().fromJson(
                            fullJson,
                            SecBleResponse::class.java
                        )

                        Log.d(
                            "BLESERVICE",
                            "Second metering response = $response"
                        )

                        secondMeteringResponse = response

                        // Now we have BOTH.
                        combineResponses()

                        // Next polling cycle should expect first response again.
                        waitingForSecondResponse = false
                    }


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
                Log.e("BLESERVICE", "Notification Descriptor Write Success")

                pollingJob?.cancel()
                pollingJob = serviceScope.launch {
                    while (true) {
                        requestMeteringData()
                        delay(6000.milliseconds) // Wait 3 seconds
                    }
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
                        val descriptor = meteringCharacteristic.getDescriptor(ccdUuid)
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
            pollingJob?.cancel()
            setWriteSuccess(false)
            notificationData.value = null

        }

    }


    private fun requestMeteringData() {
        val gatt = bluetoothGatt ?: return
        if (!ReusableFunctions.checkPermission(context)) return

        val service = gatt.getService(serviceUuid) ?: return
        val meteringCharacteristic = service.getCharacteristic(meteringUUID) ?: return

        val data = BleWrite("insta_metering_check", "read")
        val json = Gson().toJson(data)
        val bytes = json.toByteArray(Charsets.UTF_8)

        meteringCharacteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        meteringCharacteristic.value = bytes
        gatt.writeCharacteristic(meteringCharacteristic)
        Log.d("BLESERVICE", "Periodic data request sent")
    }

    private fun sendSecondMeteringCommand() {
        Log.e("BLESERVICE", "Secound Metering Command Sent")

        val gatt = bluetoothGatt ?: return

        if (!ReusableFunctions.checkPermission(context)) {
            return
        }

        val service = gatt.getService(serviceUuid) ?: return

        val characteristic = service.getCharacteristic(meteringUUID) ?: return

        val data = BleWrite(
            "enrg_metering_check", "read"
        )

        val bytes = Gson().toJson(data).toByteArray(Charsets.UTF_8)

        characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT

        characteristic.value = bytes

        gatt.writeCharacteristic(characteristic)
    }

    private val combinedMeteringData =
        MutableStateFlow<CombinedMeteringData?>(null)

    val combinedMeteringDataInfo =
        combinedMeteringData.asStateFlow()

    private fun combineResponses() {

        val first = firstMeteringResponse ?: return
        val second = secondMeteringResponse ?: return

        val combined = CombinedMeteringData(
            voltage = first.V,
            current = first.I,
            activePower = first.P,
            apparentPower = first.S,
            reactivePower = first.Q,
            frequency = first.Fq,

            pf = second.Pt,
            st = second.St,
            qt = second.Qt
        )

        combinedMeteringData.value = combined
    }
}
