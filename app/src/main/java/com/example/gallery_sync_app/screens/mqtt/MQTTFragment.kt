package com.example.gallery_sync_app.screens.mqtt

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentMqttFragBinding

class MQTTFragment : Fragment(R.layout.fragment_mqtt_frag) {
    private lateinit var mqttManager: MQTTManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {


        Log.e("MQQT","onViewCreated")
        super.onViewCreated(view, savedInstanceState)
            val binding = FragmentMqttFragBinding.bind(view)
            mqttManager=MQTTManager()
            mqttManager.connect()
            binding.subText1.setOnClickListener {
                mqttManager.publish()

            }
        }catch (e: Exception){
            Log.e("MQQT","error ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttManager.disconnect()

    }

}