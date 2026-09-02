package com.example.gallery_sync_app.screens.data

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "images")
data class Images(
    @PrimaryKey(autoGenerate = true)
    val userUid: Int=0,
    @ColumnInfo("imageUrl") val url: String,
    @ColumnInfo("imageId") val imageId: String,
    @ColumnInfo("title") val title: String
)
