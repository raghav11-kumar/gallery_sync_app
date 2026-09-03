package com.example.gallery_sync_app.screens.websockets

import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketsManager {
//    private val client= OkHttpClient()
//    private var webSocket: WebSocket?=null
//    private val gson= Gson()
//    fun connect(){
//        val request = Request.Builder()
//            .url("ws://10.0.2.2:8080/socket")
//            .build()
//
//        webSocket=client.newWebSocket(request,object : WebSocketListener(){
//            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
//                super.onClosed(webSocket, code, reason)
//                Log.e("WebSockets","Closed")
//            }
//
//            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//                super.onClosing(webSocket, code, reason)
//                Log.e("WebSockets","One Of Them Is Closed")
//
//            }
//
//            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//                super.onFailure(webSocket, t, response)
//                Log.e("WebSockets","Failed To Communicate${t.message}")
//
//            }
//
//            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//                super.onMessage(webSocket, bytes)
//                Log.e("WebSockets","Message Received")
//
//                val json=bytes.utf8()
//                val message = gson.fromJson(
//                    json,
//                    WebSocketResponse::class.java
//                )
//            }
//
//            override fun onOpen(webSocket: WebSocket, response: Response) {
//                super.onOpen(webSocket, response)
//                Log.e("WebSockets","Communication is opened")
//            }
//        })
//    }
//    fun sendMessage(message: String){
//
//        webSocket?.send(message)
//    }
//    fun close(){
//        webSocket?.close(1000,"Closing")
//        webSocket=null
//    }

}