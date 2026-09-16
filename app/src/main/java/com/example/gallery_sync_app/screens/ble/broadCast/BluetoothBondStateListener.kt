package com.example.gallery_sync_app.screens.ble.broadCast

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BluetoothBondStateListener(
    private val onBondStateChanged: (BluetoothDevice, Int) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(
        context: Context?,
        intent: Intent?
    ) {

        if (intent?.action != BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
            return
        }

        val device =
            intent.getParcelableExtra<BluetoothDevice>(
                BluetoothDevice.EXTRA_DEVICE
            ) ?: return

        val bondState =
            intent.getIntExtra(
                BluetoothDevice.EXTRA_BOND_STATE,
                BluetoothDevice.ERROR
            )

        Log.d(
            "BluetoothBondState",
            "Device=${device.address}, state=$bondState"
        )

        onBondStateChanged(device, bondState)
    }
}