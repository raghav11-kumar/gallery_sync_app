package com.example.gallery_sync_app.screens.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentBLEDeviceBinding
import com.example.gallery_sync_app.screens.ble.data.BleDeviceInfo


class BLEDevice : Fragment(R.layout.fragment_b_l_e_device) {

    private lateinit var binding: FragmentBLEDeviceBinding

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private lateinit var bleAdapter: com.example.gallery_sync_app.screens.ble.BluetoothAdapter

    private val bleDevices = mutableListOf<BleDeviceInfo>()

    private val handler = Handler(Looper.getMainLooper())

    // ----------------------------------------------------
    // Enable Bluetooth launcher
    // ----------------------------------------------------

    private val enableBluetoothLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (bluetoothAdapter.isEnabled) {

                Log.d("BLEDevice", "Bluetooth enabled")

                requestBluetoothPermissions()

            } else {

                Log.d("BLEDevice", "Bluetooth was not enabled")
            }
        }


    // ----------------------------------------------------
    // Bluetooth permission launcher
    // ----------------------------------------------------

    private val bluetoothPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val scanGranted =
                permissions[Manifest.permission.BLUETOOTH_SCAN] == true

            val connectGranted =
                permissions[Manifest.permission.BLUETOOTH_CONNECT] == true

            if (scanGranted && connectGranted) {

                Log.d("BLEDevice", "Bluetooth permissions granted")

                scanDevices()

            } else {

                Log.e(
                    "BLEDevice",
                    "Bluetooth permissions denied"
                )
            }
        }


    // ----------------------------------------------------
    // onViewCreated
    // ----------------------------------------------------

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBLEDeviceBinding.bind(view)

        setupBluetooth()

        setupRecyclerView()
        startBleProcess()


        binding.bleClick.setOnClickListener {
            startBleProcess()

        }
    }


    // ----------------------------------------------------
    // Bluetooth setup
    // ----------------------------------------------------

    private fun setupBluetooth() {

        bluetoothManager =
            requireContext().getSystemService(
                BluetoothManager::class.java
            )

        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null) {

            Log.e(
                "BLEDevice",
                "Bluetooth is not supported on this device"
            )

            return
        }

        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner!!
    }


    // ----------------------------------------------------
    // RecyclerView setup
    // ----------------------------------------------------

    private fun setupRecyclerView() {

        bleAdapter = com.example.gallery_sync_app.screens.ble.BluetoothAdapter(bleDevices,requireContext())

        binding.bleRec.apply {

            adapter = bleAdapter

            layoutManager =
                LinearLayoutManager(requireContext())
        }
    }


    // ----------------------------------------------------
    // Start BLE process
    // ----------------------------------------------------

    private fun startBleProcess() {

        if (!bluetoothAdapter.isEnabled) {

            Log.d(
                "BLEDevice",
                "Bluetooth is OFF. Asking user to enable it."
            )

            val enableBluetoothIntent =
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            enableBluetoothLauncher.launch(
                enableBluetoothIntent
            )

        } else {

            Log.d(
                "BLEDevice",
                "Bluetooth already enabled."
            )

            requestBluetoothPermissions()
        }
    }


    // ----------------------------------------------------
    // Request Bluetooth permissions
    // ----------------------------------------------------

    private fun requestBluetoothPermissions() {

        val permissions = arrayOf(

            Manifest.permission.BLUETOOTH_SCAN,

            Manifest.permission.BLUETOOTH_CONNECT
        )

        bluetoothPermissionLauncher.launch(
            permissions
        )
    }


    // ----------------------------------------------------
    // Scan BLE devices
    // ----------------------------------------------------

    private fun scanDevices() {

        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            Log.e(
                "BLEDevice",
                "BLUETOOTH_SCAN permission not granted"
            )

            return
        }

        bleDevices.clear()

        bleAdapter.notifyDataSetChanged()

        binding.scanProgress.visibility = View.VISIBLE

        binding.emptyView.visibility = View.GONE

        Log.d(
            "BLEDevice",
            "Starting BLE scan..."
        )

        bluetoothLeScanner.startScan(
            scanCallback
        )


        // Stop scanning after 10 seconds
        handler.postDelayed({

            stopScan()

        }, 10_000)
    }


    // ----------------------------------------------------
    // Stop BLE scan
    // ----------------------------------------------------

    private fun stopScan() {

        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        bluetoothLeScanner.stopScan(
            scanCallback
        )

        binding.scanProgress.visibility =
            View.GONE

        if (bleDevices.isEmpty()) {

            binding.emptyView.visibility =
                View.VISIBLE
        }

        Log.d(
            "BLEDevice",
            "BLE scan stopped"
        )
    }


    // ----------------------------------------------------
    // BLE Scan Callback
    // ----------------------------------------------------

    private val scanCallback =
        object : ScanCallback() {

            override fun onScanResult(
                callbackType: Int,
                result: ScanResult
            ) {

                super.onScanResult(
                    callbackType,
                    result
                )

                if (!isAdded) {
                    return
                }

                if (
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }


                val device = result.device

                val macAddress =
                    device.address

                val deviceName =
                    device.name ?: "Unknown Device"

                val rssi =
                    result.rssi


                // Prevent duplicate devices
                if (
                    bleDevices.none {
                        it.macAddress == macAddress
                    }
                ) {

                    if (deviceName != "Unknown Device") {

                        val bleDevice =
                            BleDeviceInfo(
                                deviceName,
                                macAddress,
                                rssi
                            )

                        bleDevices.add(
                            bleDevice
                        )

                        bleAdapter.notifyItemInserted(
                            bleDevices.size - 1
                        )

                        binding.emptyView.visibility =
                            View.GONE

                        Log.d(
                            "BLEDevice",
                            "Device found: $deviceName - $macAddress"
                        )
                    }
                }
            }


            override fun onScanFailed(
                errorCode: Int
            ) {

                super.onScanFailed(
                    errorCode
                )

                Log.e(
                    "BLEDevice",
                    "BLE scan failed: $errorCode"
                )

                binding.scanProgress.visibility =
                    View.GONE
            }
        }


    // ----------------------------------------------------
    // Cleanup
    // ----------------------------------------------------

    override fun onDestroyView() {

        handler.removeCallbacksAndMessages(null)

        if (
            ::bluetoothLeScanner.isInitialized &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            bluetoothLeScanner.stopScan(
                scanCallback
            )
        }

        super.onDestroyView()
    }
}