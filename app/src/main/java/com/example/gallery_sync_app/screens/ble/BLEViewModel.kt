package com.example.gallery_sync_app.screens.ble

import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery_sync_app.screens.apis.Res
import com.example.gallery_sync_app.screens.repository.DataBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BLEViewModel @Inject constructor(
    private val repository: DataBaseRepository
): ViewModel() {
    private val ktorRes=MutableStateFlow<Res?>(null)
    val ktorResult=ktorRes

    private val _scannedDevices = MutableStateFlow<List<ScanResult>>(emptyList())
    val scannedDevices = _scannedDevices.asStateFlow()

    fun addScanResult(result: ScanResult) {
        _scannedDevices.update { currentList ->
            val index = currentList.indexOfFirst { it.device.address == result.device.address }
            if (index != -1) {
                currentList.toMutableList().apply { this[index] = result }
            } else {
                currentList + result
            }
        }
    }

    fun clearScanResults() {
        _scannedDevices.value = emptyList()
    }

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