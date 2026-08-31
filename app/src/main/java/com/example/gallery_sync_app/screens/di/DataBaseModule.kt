package com.example.gallery_sync_app.screens.di

import com.example.gallery_sync_app.screens.repository.DataBaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Singleton
    @Provides
    fun provideDataBaseRepo(fireBase: FirebaseFirestore)= DataBaseRepository(fireBase)
}