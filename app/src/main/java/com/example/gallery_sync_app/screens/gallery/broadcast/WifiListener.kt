package com.example.gallery_sync_app.screens.gallery.broadcast

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import com.google.firebase.logger.Logger

class WifiListener(
 val   onNoInternet:()-> Unit
) : BroadcastReceiver()  {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("WifiListener","Wifi Listener Called")
        if(intent==null)
            return
        if(intent.action== WifiManager.WIFI_STATE_CHANGED_ACTION){
            val state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,                WifiManager.WIFI_STATE_UNKNOWN
            )

            when(state){
                WifiManager.WIFI_STATE_ENABLED->{
                    Log.e("WifiListener","Wifi Enabled")
                }
                WifiManager.WIFI_STATE_DISABLED->{
                    onNoInternet()
                    Log.e("WifiListener","Wifi Disabled")
                }

            }

        }
    }
}