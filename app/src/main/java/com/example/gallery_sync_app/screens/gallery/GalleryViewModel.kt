package com.example.gallery_sync_app.screens.gallery

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery_sync_app.screens.data.ImagBBResponse
import com.example.gallery_sync_app.screens.data.ImageInfo
import com.example.gallery_sync_app.screens.data.Images
import com.example.gallery_sync_app.screens.repository.DataBaseRepository
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.URI
import javax.inject.Inject
@HiltViewModel
class GalleryViewModel @Inject constructor(val repository: DataBaseRepository) : ViewModel(){
    val imageList: MutableStateFlow<List<Images>> = MutableStateFlow(emptyList())
    private val galleryOpenClicked= MutableStateFlow<Boolean>(false)
    val galleryOpen=galleryOpenClicked

    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading
    fun openGallery(){
        galleryOpenClicked.value=true
    }
    fun closeGallery(){
        galleryOpenClicked.value=false
    }
    private val editClick= MutableStateFlow<Boolean>(false)
    val editClickOpen=editClick
    fun openEdit(){
        editClick.value = !editClick.value
    }



    init {
        viewModelScope.launch {
            repository.getImagesList().collect { users ->
                imageList.value = users
            }
        }
    }
    private val errorFlow = MutableSharedFlow<String>()
    val errorFlowing = errorFlow.asSharedFlow()


    fun saveImage(uri: Uri) {
        val apiKey = "f06041a98c3e3556f51266c55a27e4b6"
        val multipartData = repository.convertUriToImage(uri)
        multipartData.onSuccess {
            viewModelScope.launch {
                _isUploading.value = true
                val response = repository.sendImageForRecyclerView(apiKey = apiKey, it)
                response.onFailure { error ->
                    Log.e("AuthVm", "Failed TO Send Image${error.message}")
                    _isUploading.value = false
                        errorFlow.emit(error.message ?: "Update Failed")

                }
                response.onSuccess {
                    Log.e("GalleryVm","SuccessFully Send The Image ${it}")
                    _isUploading.value = false
                }
            }
        }.onFailure {
             Log.e("GalleryVm","Failed TO Convert Image ${it.message}")
        }
    }
    fun deleteImage(image: Images){
        viewModelScope.launch {

            repository.deleteImage(image)

        }
    }
}
