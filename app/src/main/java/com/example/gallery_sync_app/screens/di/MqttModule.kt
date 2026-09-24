package com.example.gallery_sync_app.screens.di

import com.example.gallery_sync_app.screens.mqtt.MQTTManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MqttModule {
    @Singleton
    @Provides
    fun provideMqttManager()= MQTTManager()
}