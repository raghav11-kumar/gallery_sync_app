package com.example.gallery_sync_app.screens.mqtt

import android.util.Log
import com.example.gallery_sync_app.screens.mqtt.data.MqttResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MQTTManager {

    private val TAG = "MQTT"
    private val serverUrl = "ssl://9601e0f2642f40ad9f88d34a4f33cc15.s1.eu.hivemq.cloud:8883"
    private val password = "Blaze@4321"
    private val gson = Gson()

    private val mqttResponse = MutableSharedFlow<MqttResponse?>(2)
    val mqttEvents = mqttResponse.asSharedFlow()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val connected = MutableStateFlow(false)
    val isConnected = connected.asStateFlow()

    private val clientId =
        "android_${System.currentTimeMillis()}"
    private val client =
        MqttClient(
            serverUrl,
            clientId,
            MemoryPersistence()
        )

    fun connect() {
        Log.e(TAG, "Fun Called")
        if (client.isConnected) {
            Log.d(TAG, "Already connected")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {

            try {


                val options = MqttConnectOptions().apply {
                    keepAliveInterval = 80
                    connectionTimeout = 80
                    isAutomaticReconnect = true
                    isCleanSession = true
                    userName = "listner"
                    password = this@MQTTManager.password.toCharArray()
                }

                client.setCallback(object : MqttCallbackExtended {

                    override fun connectComplete(
                        reconnect: Boolean,
                        serverURI: String?
                    ) {
                        connected.value = true
                        Log.d(
                            TAG,
                            "Connected: $serverURI"
                        )
                        subscribe("test/events", 1)

                    }

                    override fun connectionLost(
                        cause: Throwable?
                    ) {
                        connected.value = false
                        Log.e(
                            TAG,
                            "Connection lost",
                            cause
                        )
                    }

                    override fun messageArrived(
                        topic: String?,
                        message: MqttMessage?
                    ) {
                        Log.d(TAG, "Message Arrived on topic: $topic")

                        val json = message?.payload?.decodeToString() ?: message?.toString() ?: ""
                        Log.d(TAG, "Raw Message String: $json")

                        try {
                            val parsedResponse = gson.fromJson(
                                json,
                                MqttResponse::class.java
                            )
                            Log.d(TAG, "Parsed MqttResponse: $parsedResponse")
                            scope.launch {
                                mqttResponse.emit(parsedResponse)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing JSON to MqttResponse", e)
                        }
                    }

                    override fun deliveryComplete(
                        token: org.eclipse.paho.client.mqttv3.IMqttDeliveryToken?
                    ) {

                        Log.d(
                            TAG,
                            "Delivery complete"
                        )
                    }
                })

                client.connect(options)

                Log.d(
                    TAG,
                    "Connected successfully"
                )


            } catch (e: Exception) {

                Log.e(
                    TAG,

                    "Connection failed",
                    e
                )
            }
        }
    }

    fun subscribe(
        topic: String,
        qos: Int = 1
    ) {

        try {

            if (!client.isConnected) {
                Log.e(
                    TAG,
                    "Cannot subscribe. Client not connected."
                )
                return
            }

            client.subscribe(topic, qos)

            Log.d(
                TAG,
                "Subscribed to $topic"
            )

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Subscribe failed",
                e
            )
        }
    }

    fun disconnect() {

        try {

            if (client.isConnected) {

                client.disconnect()
                connected.value = false

                Log.d(
                    TAG,
                    "Disconnected"
                )
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Disconnect failed",
                e
            )
        }
    }
}