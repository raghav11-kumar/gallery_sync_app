package com.example.gallery_sync_app.screens

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.gallery_sync_app.screens.services.NotificationService.Companion.counter_notif_id
import com.example.gallery_sync_app.screens.websockets.WebSocketsManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }


}