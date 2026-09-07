package com.example.gallery_sync_app.screens.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import com.example.gallery_sync_app.R

class NotificationService(
    private val context: Context
) {
    fun showNotification(title: String, content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val channel = NotificationChannel(
                "${counter_notif_id}", "notiChannel${System.currentTimeMillis()}", NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =context. getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            val notification = Notification.Builder(
                context,
                "${counter_notif_id}"
            )
                .setSmallIcon(R.drawable.home_icon_foreground)
                .setContentTitle(title)
                .setContentText(content)
                .build()
            notificationManager.notify(counter_notif_id++, notification)
        }
    }

    companion object {
        var counter_notif_id = 0

    }
}