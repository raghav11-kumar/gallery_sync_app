package com.example.gallery_sync_app.screens.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationService: NotificationService
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCMNewToken", "The new Token ${token}")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        var title = message.notification?.title
        var body = message.notification?.body
        if (title == null) {
            title = message.data["title"] ?: "No Title"

        }
        if (body == null) {
            body = message.data["content"] ?: message.data["body"] ?: "NO BODY"
        }
        notificationService.showNotification(title, body)
        Log.e("FCMMessage", "${title}:${body}")

    }

    private fun createBasicNotification(title: String, message: String) {

    }
}