package com.example.gallery_sync_app.screens.data.roomDataBase

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.gallery_sync_app.screens.data.Images

@Database(entities = [Users::class, Images::class], version = 4, exportSchema = false)
abstract class RoomDataBaseImp : RoomDatabase(){
    abstract fun userDao(): UserDao
}