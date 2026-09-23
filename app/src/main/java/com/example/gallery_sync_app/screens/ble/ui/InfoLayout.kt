package com.example.gallery_sync_app.screens.ble.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentInfoLayoutBinding
import com.example.gallery_sync_app.screens.ble.BluetoothService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InfoLayout : Fragment(R.layout.fragment_info_layout) {
    lateinit var binding: FragmentInfoLayoutBinding

    @Inject
    lateinit var bluetoothService: BluetoothService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentInfoLayoutBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            bluetoothService.deviceInformation.collect {
                it?.let {
                    binding.devName.text = it.deviceName
                    binding.macAddress.text = it.macAddress
                    binding.serUUid.text = it.serviceUUID
                    binding.charUUid.text = it.charUUID
                    binding.bondStateId.text = it.bondState
                    binding.devStatus.text = it.connected

                }


            }


        }

    }


}

