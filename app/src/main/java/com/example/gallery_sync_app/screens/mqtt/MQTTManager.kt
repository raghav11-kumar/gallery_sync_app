package com.example.gallery_sync_app.screens.mqtt

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MQTTManager{

    private val serverUrl = "tcp://10.30.41.123:1883"

    private val clientId =
        "android_${System.currentTimeMillis()}"

    private val client = MqttClient(
        serverUrl,
        clientId,
        MemoryPersistence()
    )

    fun connect() {

        if (client.isConnected) {
            Log.d("MQTT", "Already connected")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val options = MqttConnectOptions().apply {
                    keepAliveInterval = 60
                    connectionTimeout = 60
                    isAutomaticReconnect = true
                    isCleanSession = true
                }

                client.setCallback(object : MqttCallbackExtended {

                    override fun connectComplete(
                        reconnect: Boolean,
                        serverURI: String?
                    ) {

                        Log.d(
                            "MQTT",
                            "Connected: $serverURI"
                        )

                        subscribe("test/home")
                    }

                    override fun connectionLost(
                        cause: Throwable?
                    ) {

                        Log.e(
                            "MQTT",
                            "Connection lost",
                            cause
                        )
                    }

                    override fun messageArrived(
                        topic: String?,
                        message: MqttMessage?
                    ) {

                        Log.d(
                            "MQTT",
                            "Topic: $topic"
                        )

                        Log.d(
                            "MQTT",
                            "Message: ${message?.toString()}"
                        )
                    }

                    override fun deliveryComplete(
                        token: org.eclipse.paho.client.mqttv3.IMqttDeliveryToken?
                    ) {

                        Log.d(
                            "MQTT",
                            "Delivery complete"
                        )
                    }
                })

                client.connect(options)

                Log.d(
                    "MQTT",
                    "Connected successfully"
                )

                subscribe("test/home")

            } catch (e: Exception) {

                Log.e(
                    "MQTT",
                    "Connection failed",
                    e
                )
            }
        }
    }
     fun publish(){
        try {
            if (!client.isConnected) {
                Log.e(
                    "MQTT",
                    "Cannot publish. Client not connected."
                )
                return
            }
            val payload=byteArrayOf(1,2,3)
            client.publish("text/sendMessage",payload,1,true)


        }catch (e: Exception){
            Log.e("MQTT","Publish Failed${e.message}")
        }
    }

    private fun subscribe(
        topic: String,
        qos: Int = 1
    ) {

        try {

            if (!client.isConnected) {
                Log.e(
                    "MQTT",
                    "Cannot subscribe. Client not connected."
                )
                return
            }

            client.subscribe(topic, qos)

            Log.d(
                "MQTT",
                "Subscribed to $topic"
            )

        } catch (e: Exception) {

            Log.e(
                "MQTT",
                "Subscribe failed",
                e
            )
        }
    }

    fun disconnect() {

        try {

            if (client.isConnected) {

                client.disconnect()

                Log.d(
                    "MQTT",
                    "Disconnected"
                )
            }

        } catch (e: Exception) {

            Log.e(
                "MQTT",
                "Disconnect failed",
                e
            )
        }
    }
}