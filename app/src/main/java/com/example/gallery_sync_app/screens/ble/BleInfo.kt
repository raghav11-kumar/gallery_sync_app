package com.example.gallery_sync_app.screens.ble

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentBleInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BleInfo : Fragment(R.layout.fragment_ble_info) {
    private lateinit var binding: FragmentBleInfoBinding

    @Inject
    lateinit var bluetoothService: BluetoothService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBleInfoBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            bluetoothService.notificationData.collect { response ->
                response?.let {
                    val displayText = """
                        Command: ${it.cmd_test_type ?: "N/A"}
                        Action: ${it.action ?: "N/A"}
                        Status: ${it.status ?: "N/A"}
                        Value: ${it.value ?: "N/A"}
                        Message: ${it.message ?: "N/A"}
                    """.trimIndent()
                    binding.bleResponseText.text = displayText
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothService.disConnect()
    }
}
