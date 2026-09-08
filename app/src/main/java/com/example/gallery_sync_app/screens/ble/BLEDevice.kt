package com.example.gallery_sync_app.screens.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View

import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentBLEDeviceBinding

class BLEDevice : Fragment(R.layout.fragment_b_l_e_device) {



    private lateinit var binding: FragmentBLEDeviceBinding

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private var bluetoothScanner: BluetoothLeScanner? = null

    private val handler = Handler(Looper.getMainLooper())
    private val scanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(
            callbackType: Int,
            result: ScanResult
        ) {

            val device = result.device

            Log.d(
                "BLE_SCAN",
                "Name: ${device.name}, " +
                        "Address: ${device.address}, " +
                        "RSSI: ${result.rssi}"
            )
        }


        override fun onScanFailed(errorCode: Int) {

            Log.e(
                "BLE_SCAN",
                "Scan failed. Error code: $errorCode"
            )
        }

        override fun onBatchScanResults(results: List<ScanResult?>?) {
            super.onBatchScanResults(results)
        }
    }

    private val bluetoothPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val scanGranted =
                permissions[Manifest.permission.BLUETOOTH_SCAN] == true

            val connectGranted =
                permissions[Manifest.permission.BLUETOOTH_CONNECT] == true


            if (scanGranted && connectGranted) {

                Log.d(
                    "BLE_PERMISSION",
                    "Bluetooth permissions granted"
                )

                startBleScan()

            } else {

                Log.e(
                    "BLE_PERMISSION",
                    "Bluetooth permissions denied"
                )
            }
        }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBLEDeviceBinding.bind(view)


        val bluetoothManager =
            requireContext()
                .getSystemService(BluetoothManager::class.java)


        bluetoothAdapter = bluetoothManager.adapter


        bluetoothScanner =
            bluetoothAdapter.bluetoothLeScanner


        binding.bleText.setOnClickListener {

            checkBluetoothPermissions()
        }
    }
    private fun checkBluetoothPermissions() {

        // Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            if (hasBluetoothPermissions()) {

                Log.d(
                    "BLE_PERMISSION",
                    "Permissions already granted"
                )

                startBleScan()

            } else {

                requestBluetoothPermissions()
            }

        } else {

            // For Android 11 and below
            // BLE scanning requires location permission

            if (
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                startBleScan()

            } else {

                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }




    private fun requestBluetoothPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )

            bluetoothPermissionLauncher.launch(permissions)
        }
    }


    private fun hasBluetoothPermissions(): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return true
        }

        val scanPermission =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED


        val connectPermission =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED


        return scanPermission && connectPermission
    }
    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                Log.d(
                    "BLE_PERMISSION",
                    "Location permission granted"
                )

                startBleScan()

            } else {

                Log.e(
                    "BLE_PERMISSION",
                    "Location permission denied"
                )
            }
        }


    @SuppressLint("MissingPermission")
    private fun startBleScan() {

        if (!bluetoothAdapter.isEnabled) {

            Log.e(
                "BLE_SCAN",
                "Bluetooth is disabled"
            )

            return
        }


        val scanner = bluetoothScanner

        if (scanner == null) {

            Log.e(
                "BLE_SCAN",
                "BluetoothLeScanner is null"
            )

            return
        }


        Log.d(
            "BLE_SCAN",
            "Starting BLE scan..."
        )


        scanner.startScan(scanCallback)


        handler.postDelayed({

            stopBleScan()

        }, 10_000)
    }



    @SuppressLint("MissingPermission")
    private fun stopBleScan() {

        bluetoothScanner?.stopScan(scanCallback)

        Log.d(
            "BLE_SCAN",
            "BLE scan stopped"
        )
    }



    override fun onDestroyView() {

        stopBleScan()

        handler.removeCallbacksAndMessages(null)

        super.onDestroyView()
    }
}