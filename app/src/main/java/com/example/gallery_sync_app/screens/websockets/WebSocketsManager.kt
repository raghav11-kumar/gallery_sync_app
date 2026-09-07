package com.example.gallery_sync_app.screens.websockets

import android.util.Log
import androidx.activity.viewModels
import com.example.gallery_sync_app.screens.Authentication.AuthenticationViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.getValue

@Singleton
class WebSocketsManager @Inject constructor() {
    private val client= OkHttpClient()
    private var webSocket: WebSocket?=null
    private val gson= Gson()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val socketMessages= MutableSharedFlow<WebSocketResponse?>(2)
    val messageFlow=socketMessages.asSharedFlow()
    fun connect(){
        try {
            val request = Request.Builder()
                .url("ws://10.30.41.123:8081")
                .build()


            webSocket = client.newWebSocket(request, object : WebSocketListener() {
                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                    Log.e("WebSockets", "Closed")
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                    Log.e("WebSockets", "One Of Them Is Closed")

                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    Log.e("WebSockets", "Failed To Communicate${t.message}")
                    webSocket.close(1000,"Failed")
                    webSocket.cancel()
                    connect()


                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    Log.e("WebSockets", "Message Received ${text}")

                    val json = text
                    val message = gson.fromJson(
                        json,
                        WebSocketResponse::class.java
                    )
                    scope.launch {
                        socketMessages.emit(message)
                    }
                }

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    Log.e("WebSockets", "Communication is opened")
                }
            })
        }catch (webSocketException:Exception){
            Log.e("WebSockets", "Exception Occurred ${webSocketException.message}")
        }
    }
    fun sendMessage(message: String){

        webSocket?.send(message)
    }
    fun close(){
        webSocket?.close(1000,"Closing")
        webSocket=null
    }

}