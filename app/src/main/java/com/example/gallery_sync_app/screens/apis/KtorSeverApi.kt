package com.example.gallery_sync_app.screens.apis

import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface KtorSeverApi {
    @GET("v1/user/check")
    suspend fun check(): Res
}
@Serializable
data class Res(
    val name: String,
    val message: String,
    val status: Int
)
