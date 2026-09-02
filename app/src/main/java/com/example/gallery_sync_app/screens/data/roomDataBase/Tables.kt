package com.example.gallery_sync_app.screens.data.roomDataBase

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "users")
data class Users(
    @PrimaryKey
    @ColumnInfo("userUid")   val userUid: String,
    @ColumnInfo("name") val name: String?="",
    @ColumnInfo("email") val email: String?="",
    @ColumnInfo("imageUrl")val imageUrl: String?=""
)