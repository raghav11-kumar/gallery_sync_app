package com.example.gallery_sync_app.screens.ble.broadCast

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BluetoothReceiver(
    private val onBondStateChanged: ( Int) -> Unit

) : BroadcastReceiver(
) {
    //
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null)
            return

        if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
 onBondStateChanged(state)
        }

    }
}
