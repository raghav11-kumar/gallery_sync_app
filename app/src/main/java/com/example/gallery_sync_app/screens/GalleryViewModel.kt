package com.example.gallery_sync_app.screens

import androidx.lifecycle.ViewModel
import com.example.gallery_sync_app.screens.repo.Information

class GalleryViewModel : ViewModel() {
    val info= Information()
    fun getList()=info.getList()



}