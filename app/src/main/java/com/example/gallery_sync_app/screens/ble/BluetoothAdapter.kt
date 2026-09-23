package com.example.gallery_sync_app.screens.ble

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.screens.ble.data.BleDeviceInfo
import com.example.gallery_sync_app.screens.utils.ReusableFunctions

class BluetoothAdapter(
    val list: List<BleDeviceInfo>,
    val context: Context,
    private val onDeviceClick: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<BluetoothAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) {
        val data = list[position]
        holder.itemView.apply {
            holder.devName.text = data.deviceName
            holder.macAdd.text = data.macAddress
            holder.rxxi.text = data.rssi.toString()

        }
        holder.itemView.setOnClickListener {
            ReusableFunctions.DefaultAlertDialog(
                context, "Do You Want To Connect ${data.deviceName}", "Yes", "No"
            ) {
                //connect To The Device
                onDeviceClick(data.device)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val devName = view.findViewById<TextView>(R.id.deviceName)
        val macAdd: TextView = view.findViewById<TextView>(R.id.macAddress)
        val rxxi: TextView = view.findViewById<TextView>(R.id.rssiText)


    }
}