package com.example.gallery_sync_app.screens.mqtt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MqttViewModel @Inject constructor(
    private val mqttManager: MQTTManager
) : ViewModel() {

    val mqttResponse = mqttManager.mqttEvents.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    fun connect() {
        mqttManager.connect()
    }

    fun disconnect() {
        mqttManager.disconnect()
    }

    val isConnected = mqttManager.isConnected
}