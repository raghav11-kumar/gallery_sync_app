package com.example.gallery_sync_app.screens.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentBLEDeviceBinding
import com.example.gallery_sync_app.screens.ble.data.BleDeviceInfo
import kotlinx.coroutines.launch
import androidx.navigation.findNavController
import com.example.gallery_sync_app.screens.repository.DataBaseRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BLEDevice : Fragment(R.layout.fragment_b_l_e_device) {

    private lateinit var binding: FragmentBLEDeviceBinding

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private lateinit var bleAdapter: com.example.gallery_sync_app.screens.ble.BluetoothAdapter

    private val bleDevices = mutableListOf<BleDeviceInfo>()
    @Inject
    lateinit var bluetoothService: BluetoothService

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var device: BluetoothDevice

    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (bluetoothAdapter.isEnabled) {

            Log.d("BLEDevice", "Bluetooth enabled")

            requestBluetoothPermissions()

        } else {

            Log.d("BLEDevice", "Bluetooth was not enabled")
        }
    }


    private val bluetoothPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val scanGranted = permissions[Manifest.permission.BLUETOOTH_SCAN] == true
        val connectGranted = permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        // On Android 11 and below, location permission is strictly required for BLE scanning.
        // On Android 12+, BLUETOOTH_SCAN is required.
        val canScan = scanGranted || fineLocationGranted || coarseLocationGranted

        if (canScan && connectGranted) {
            Log.d("BLEDevice", "Bluetooth / Location permissions granted")
            scanDevices()
        } else {
            Log.e("BLEDevice", "Bluetooth / Location permissions denied")
        }
    }


    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        try {

            super.onViewCreated(view, savedInstanceState)

            binding = FragmentBLEDeviceBinding.bind(view)

            setupBluetooth()

            setupRecyclerView()
            startBleProcess()


            binding.bleClick.setOnClickListener {
                startBleProcess()
            }
            viewLifecycleOwner.lifecycleScope.launch {
                bluetoothService.writeSuccess.collect {
                    if (it) {
                        view.findNavController().navigate(R.id.navigateBleToBleInfo)
                    }

                }
            }
        }catch (e: Exception){
            Log.e("BLE","FAILED ONViewCreated ${e.message}")
        }

    }

    private fun setupBluetooth() {

        bluetoothManager = requireContext().getSystemService(
            BluetoothManager::class.java
        )

        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null) {

            Log.e(
                "BLEDevice", "Bluetooth is not supported on this device"
            )

            return
        }

        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner!!
    }


    private fun setupRecyclerView() {

        bleAdapter = BluetoothAdapter(bleDevices, requireContext()) {
            checkAndConnect(it)
        }


        binding.bleRec.apply {

            adapter = bleAdapter

            layoutManager = LinearLayoutManager(requireContext())
        }

    }


    private fun startBleProcess() {

        if (!bluetoothAdapter.isEnabled) {

            Log.d(
                "BLEDevice", "Bluetooth is OFF. Asking user to enable it."
            )

            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            enableBluetoothLauncher.launch(
                enableBluetoothIntent
            )

        } else {

            Log.d(
                "BLEDevice", "Bluetooth already enabled."
            )

            requestBluetoothPermissions()
        }
    }


    private fun requestBluetoothPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )

        // Add location permissions for backward compatibility / background BLE discovery support
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        bluetoothPermissionLauncher.launch(permissions.toTypedArray())
    }


    private fun scanDevices() {
        // We check either BLUETOOTH_SCAN or ACCESS_FINE_LOCATION to support both Android 12+ and legacy versions securely.
        val hasScanPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        val hasLocationPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!hasScanPermission && !hasLocationPermission) {
            Log.e("BLEDevice", "No scanning permissions granted (BLUETOOTH_SCAN or ACCESS_FINE_LOCATION)")
            return
        }

        bleDevices.clear()
        bleAdapter.notifyDataSetChanged()
        binding.scanProgress.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE

        Log.d("BLEDevice", "Starting BLE scan...")
        bluetoothLeScanner.startScan(scanCallback)

        // Stop scanning after 10 seconds
        handler.postDelayed({
            stopScan()
        }, 10_000)
    }


    private fun stopScan() {

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        bluetoothLeScanner.stopScan(
            scanCallback
        )

        binding.scanProgress.visibility = View.GONE

        if (bleDevices.isEmpty()) {

            binding.emptyView.visibility = View.VISIBLE
        }

        Log.d(
            "BLEDevice", "BLE scan stopped"
        )
    }


    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(
            callbackType: Int, result: ScanResult
        ) {

            super.onScanResult(
                callbackType, result
            )

            if (!isAdded) {
                return
            }

            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }


            var device = result.device


            val macAddress = device.address

            val deviceName = device.name ?: "Unknown Device"

            val rssi = result.rssi


            // Prevent duplicate devices
            if (bleDevices.none {
                    it.macAddress == macAddress
                }) {

                if (deviceName != "Unknown Device") {

                    val bleDevice = BleDeviceInfo(
                        device = device, deviceName, macAddress, rssi
                    )
                    bleDevices.add(
                        bleDevice
                    )
                    bleDevices.sortWith(
                        compareByDescending { it.rssiText }
                    )

                    bleAdapter.notifyItemInserted(
                        bleDevices.size - 1
                    )

                    binding.emptyView.visibility = View.GONE

                    Log.d(
                        "BLEDevice", "Device found: $deviceName - $macAddress"
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
                "BLEDevice", "BLE scan failed: $errorCode"
            )

            binding.scanProgress.visibility = View.GONE
        }
    }


    override fun onDestroyView() {

        handler.removeCallbacksAndMessages(null)

        if (::bluetoothLeScanner.isInitialized && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            bluetoothLeScanner.stopScan(
                scanCallback
            )
        }

        super.onDestroyView()
    }

    fun checkAndConnect(device: BluetoothDevice) {
        try {

            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothService.connect(device)
            } else {
                Log.e("Ble", "Need Bluetooth Connect Permission To Connect To That Device")
                requestBluetoothPermissions()
            }
        } catch (e: Exception) {
            Log.e("Ble", "Failed TO Check And Connect ${e.message}")
        }
    }

}