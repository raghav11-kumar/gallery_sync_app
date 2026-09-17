package com.example.gallery_sync_app.screens.ble

import androidx.lifecycle.ViewModel
import com.example.gallery_sync_app.screens.ble.data.BleDeviceInfo
import com.example.gallery_sync_app.screens.ble.data.DeviceInfo
import com.example.gallery_sync_app.screens.repository.DataBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class BLEViewModel @Inject constructor(
    private val repository: DataBaseRepository
) : ViewModel() {
    lateinit var bleInfo: DeviceInfo
    private val errorFlow = MutableSharedFlow<String>()
    val errorFlowing = errorFlow.asSharedFlow()

}
