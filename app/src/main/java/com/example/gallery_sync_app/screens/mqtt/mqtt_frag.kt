package com.example.gallery_sync_app.screens.mqtt

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentMqttFragBinding

class mqtt_frag : Fragment(R.layout.fragment_mqtt_frag) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMqttFragBinding.bind(view)
    }

}