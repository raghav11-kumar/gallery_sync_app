package com.example.gallery_sync_app.screens.di

import android.content.Context
import com.example.gallery_sync_app.screens.ble.BluetoothService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BleModule {
    @Singleton
    @Provides
    fun provideBluetoothService(@ApplicationContext context: Context) = BluetoothService(
        context =context
    )
}