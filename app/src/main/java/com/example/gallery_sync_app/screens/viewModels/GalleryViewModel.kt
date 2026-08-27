package com.example.gallery_sync_app.screens.viewModels

import androidx.lifecycle.ViewModel
import com.example.gallery_sync_app.screens.repo.Information

class GalleryViewModel : ViewModel() {
    val info= Information()
    fun getList()=info.getList()




}