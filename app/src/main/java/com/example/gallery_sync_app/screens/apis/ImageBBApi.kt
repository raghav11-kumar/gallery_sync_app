package com.example.gallery_sync_app.screens.apis

import com.example.gallery_sync_app.screens.data.ImagBBResponse
import com.example.gallery_sync_app.screens.data.ImageInfo
import com.example.gallery_sync_app.screens.data.roomDataBase.Users
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImageBBApi {
    @Multipart
    @POST("/1/upload")
    suspend fun postImage(
        @Query("key") key: String,
        @Part image: MultipartBody.Part
    ): ImagBBResponse

}
@Serializable
data class res(
    val data: String,
    val d2: String
)