package com.example.gallery_sync_app.screens.data.roomDataBase

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.gallery_sync_app.screens.data.UserData
import kotlinx.coroutines.flow.Flow
@Dao
interface UserDao{
    @Insert
    suspend fun InsertUser(userData: Users)

    @Query("UPDATE users  SET imageUrl=:imageUrl WHERE userUid=:userId ")
    suspend fun updateImageUrl(
        userId: String,
        imageUrl: String
    )
    @Query("SELECT * FROM users WHERE  userUid=:uid")
    suspend fun getUser(uid: String): Users




}