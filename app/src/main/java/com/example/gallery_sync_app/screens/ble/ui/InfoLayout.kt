package com.example.gallery_sync_app.screens.ble.ui

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentInfoLayoutBinding
import com.example.gallery_sync_app.screens.ble.BLEViewModel
import com.example.gallery_sync_app.screens.ble.BluetoothService
import com.example.gallery_sync_app.screens.ble.broadCast.BluetoothReceiver
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class InfoLayout : Fragment(R.layout.fragment_info_layout) {
    lateinit var binding: FragmentInfoLayoutBinding
    private val bleVm: BLEViewModel by activityViewModels()

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

