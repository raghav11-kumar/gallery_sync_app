package com.example.gallery_sync_app.screens.data.roomDataBase

import androidx.annotation.ReplaceWith
import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.gallery_sync_app.screens.data.Images
import com.example.gallery_sync_app.screens.data.UserData
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.Flow
@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertUser(userData: Users)

    @Query("UPDATE users  SET imageUrl=:imageUrl WHERE userUid=:userId ")
    suspend fun updateImageUrl(
        userId: String,
        imageUrl: String
    )
    @Query("SELECT * FROM users WHERE  userUid=:uid")
     fun getUser(uid: String): Flow<Users>
     @Query("SELECT * FROM users")
     fun getImageList(): Flow<List<Users>>
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun saveImage(image: Images)
     @Query("SELECT * FROM Images")
     fun getSavedImages():Flow<List<Images>>
     @Delete()
     suspend fun delete(image: Images)



}