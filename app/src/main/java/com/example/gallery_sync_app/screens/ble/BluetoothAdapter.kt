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
) : RecyclerView.Adapter<BluetoothAdapter.viewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)


        return viewHolder(view)
    }

    override fun onBindViewHolder(
        holder: viewHolder, position: Int
    ) {
        val data = list[position]
        holder.itemView.apply {
            holder.devName.text = data.deviceName
            holder.macAdd.text = data.macAddress
            holder.rxxi.text = data.rssiText.toString()

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

    class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val devName = view.findViewById<TextView>(R.id.deviceName)
        val macAdd = view.findViewById<TextView>(R.id.macAddress)
        val rxxi = view.findViewById<TextView>(R.id.rssiText)


    }
}