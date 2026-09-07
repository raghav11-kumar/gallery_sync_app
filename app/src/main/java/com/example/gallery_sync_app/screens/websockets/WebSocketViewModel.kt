package com.example.gallery_sync_app.screens.websockets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WebSocketViewModel @Inject constructor(
    private val webSocketManager: WebSocketsManager
): ViewModel() {
    init {

        webSocketManager.connect()
    }
      val webSockResp= webSocketManager.messageFlow.stateIn(
          scope = viewModelScope,
          started = SharingStarted.Lazily,
          initialValue = null
      )

}