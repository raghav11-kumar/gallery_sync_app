package com.example.gallery_sync_app.screens.ble.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentDataLayoutBinding
import com.example.gallery_sync_app.screens.ble.BluetoothService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DataLayout : Fragment(R.layout.fragment_data_layout) {
    lateinit var binding: FragmentDataLayoutBinding

    @Inject
    lateinit var bleService: BluetoothService
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDataLayoutBinding.bind(view)
        viewLifecycleOwner.lifecycleScope.launch {
            bleService.isConnectedInfo.collect {
                if (it) {
                    binding.conHead.text = getString(R.string.connected)
                } else {
                    binding.conHead.text = getString(R.string.notCon)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            bleService.notificationDataInfo.collect { data ->
                data?.let {
                    binding.volDet.text = it.V.toString()
                    binding.currDet.text = it.I.toString()
                    binding.actPValue.text = it.P.toString()
                    binding.apparValue.text = it.S.toString()
                    binding.RpValue.text = it.Q.toString()
                    binding.lpValue.text = it.Fq.toString()


                }
            }
        }
    }


}
