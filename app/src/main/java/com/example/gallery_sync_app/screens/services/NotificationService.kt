package com.example.gallery_sync_app.screens.services

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.gallery_sync_app.R

class NotificationService(
    private val context: Context
) {
    fun showNotification(title: String, content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notification = Notification.Builder(
                context,
                "${counter_notif_id}"
            )
                .setSmallIcon(R.drawable.c)
                .setContentTitle(title)
                .setContentText(content)
                .build()
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
        }
    }

    companion object {
        var counter_notif_id = 0

    }
}