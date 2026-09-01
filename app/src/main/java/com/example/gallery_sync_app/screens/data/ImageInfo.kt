package com.example.gallery_sync_app.screens.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class ImageInfo(
    val id: String,
    val title: String,
    val url: String,
    val display_url: String
)
data class ImagBBResponse(
    val data: ImageInfo
)