package com.example.gallery_sync_app.screens.mqtt

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentMqttFragBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MQTTFragment : Fragment(R.layout.fragment_mqtt_frag) {

    private val mqttVm: MqttViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            Log.d("MQTT", "onViewCreated")
            super.onViewCreated(view, savedInstanceState)
            val binding = FragmentMqttFragBinding.bind(view)



            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mqttVm.mqttResponse.collect { response ->
                        response?.let {
                            binding.eventId.text = it.eventId ?: "-"
                            binding.sourceId.text = it.source ?: "-"
                            binding.messageID.text = it.message ?: "-"
                            binding.timeStampId.text = it.timeStamp ?: "-"
                            binding.eventData.text = it.eventType ?: "-"
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MQTT", "Error in onViewCreated: ${e.message}", e)
        }
    }

    override fun onStart() {
        super.onStart()
        mqttVm.connect()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mqttVm.disconnect()
    }
}