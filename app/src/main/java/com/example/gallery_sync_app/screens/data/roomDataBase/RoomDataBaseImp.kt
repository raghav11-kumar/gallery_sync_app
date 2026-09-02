package com.example.gallery_sync_app.screens.data.roomDataBase

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(entities = [Users::class], version = 3, exportSchema = false)
abstract class RoomDataBaseImp : RoomDatabase(){
    abstract fun userDao(): UserDao
}