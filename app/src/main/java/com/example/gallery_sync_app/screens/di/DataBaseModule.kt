package com.example.gallery_sync_app.screens.di

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.gallery_sync_app.screens.apis.ImageBBApi
import com.example.gallery_sync_app.screens.data.roomDataBase.RoomDataBaseImp
import com.example.gallery_sync_app.screens.data.roomDataBase.UserDao
import com.example.gallery_sync_app.screens.repository.DataBaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.internal.platform.PlatformRegistry.applicationContext
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
        fbAuth: FirebaseAuth,
        userDao: UserDao
    ) = DataBaseRepository(
        fireBase, fbAuth, api, context,
        localDB = userDao
    )


    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RoomDataBaseImp {
        return Room.databaseBuilder(
            context,
            RoomDataBaseImp::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideUserDao(
        database: RoomDataBaseImp
    ): UserDao {
        return database.userDao()
    }
}