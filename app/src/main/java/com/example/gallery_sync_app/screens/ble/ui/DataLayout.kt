package com.example.gallery_sync_app.screens.ble.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
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

            bleService.combinedMeteringDataInfo.collect { data ->

                if (data == null) return@collect

                binding.volDet.text = data.voltage.toString()
                binding.currDet.text = data.current.toString()
                binding.actPValue.text = data.activePower.toString()
                binding.apparValue.text = data.apparentPower.toString()
                binding.RpValue.text = data.reactivePower.toString()
                binding.lpValue.text = data.frequency.toString()

                binding.ptValue.text = data.st.toString()
                binding.stVal.text = data.pf.toString()
                binding.qtVal.text = data.qt.toString()
            }
        }
    }


}
