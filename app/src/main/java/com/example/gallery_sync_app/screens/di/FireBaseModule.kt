package com.example.gallery_sync_app.screens.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FireBaseModule {
    @Singleton
    @Provides
    fun provideFireBaseAuthentication()= FirebaseAuth.getInstance()
    @Provides
    @Singleton
    fun provideFireBaseFireStore()= FirebaseFirestore.getInstance()



}