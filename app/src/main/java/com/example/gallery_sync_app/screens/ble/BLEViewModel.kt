package com.example.gallery_sync_app.screens.ble

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery_sync_app.screens.apis.Res
import com.example.gallery_sync_app.screens.repository.DataBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BLEViewModel @Inject constructor(
    private val repository: DataBaseRepository
): ViewModel() {
    private val ktorRes=MutableStateFlow<Res?>(null)
    val ktorResult=ktorRes
    fun getInfo()
    {
        viewModelScope.launch {
            val response=repository.getKtorRes()
            response.onFailure {
                Log.e("BLEVM","The Response Failed ${it.message}")

            }
            response.onSuccess {
                Log.e("BLEVM","The Response From Ktor Server ${it}")

            }
        }
    }
}