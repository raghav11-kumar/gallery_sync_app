package com.example.gallery_sync_app.screens.di

import android.content.Context
import com.example.gallery_sync_app.screens.apis.ImageBBApi
import com.example.gallery_sync_app.screens.repository.DataBaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Singleton
    @Provides
    fun provideDataBaseRepo(
        fireBase: FirebaseFirestore,
        api: ImageBBApi,
        @ApplicationContext context: Context,
        fbAuth: FirebaseAuth
    ) = DataBaseRepository(fireBase, fbAuth, api, context)
}