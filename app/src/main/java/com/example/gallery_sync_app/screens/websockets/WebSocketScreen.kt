package com.example.gallery_sync_app.screens.websockets

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentWebSocketScreenBinding
import com.example.gallery_sync_app.screens.Authentication.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
class WebSocketScreen : Fragment(R.layout.fragment_web_socket_screen){
    private lateinit var binding: FragmentWebSocketScreenBinding
    private val webSocketViewModel: WebSocketViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWebSocketScreenBinding.bind(view)

        
        viewLifecycleOwner.lifecycleScope.launch {
            webSocketViewModel.webSockResp.collect { response ->
                if (response != null) {
                    binding.volDet.text = response.V
                    binding.currDet.text=response.I
                    binding.actPValue.text=response.P
                    binding.apparValue.text=response.S
                    binding.RpValue.text=response.Q
                    binding.dsValue.text=response.device_state
                    binding.lpValue.text=response.d_ts
                    binding.actPValue.text=response.Pt
                   binding.enValue.text=response.St
                    binding.apparValue.text=response.St
                    binding.RpValue.text=response.Qt
                }

            }
        }
    }

}