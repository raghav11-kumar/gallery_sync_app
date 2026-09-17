package com.example.gallery_sync_app.screens.ble.ui

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentBleInfoBinding
import com.example.gallery_sync_app.screens.ble.BluetoothService
import com.example.gallery_sync_app.screens.ble.broadCast.BluetoothReceiver
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BleInfo : Fragment(R.layout.fragment_ble_info) {
    private lateinit var binding: FragmentBleInfoBinding

    @Inject
    lateinit var bluetoothService: BluetoothService
    private lateinit var bluetoothReceiver: BluetoothReceiver


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBleInfoBinding.bind(view)
        val listOfImages = listOf(
            R.drawable.message,
            R.drawable.pp,
            R.drawable.clancy,
            R.drawable.bmth,
            R.drawable.ic_launcher_foreground
        )
        bluetoothReceiver = BluetoothReceiver {
            ReusableFunctions.DefaultAlertDialog(
                requireContext(),
                "Looks Like Bluetooth Is Off. Go Back To Home Screen ?",
                "Go Back",
                "Not Now",

                ) {
                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(
                        R.id.buttonHolderFragScreen,
                        true
                    ) // Clears intermediate historical screens completely
                    .build()
                view.findNavController().navigate(R.id.buttonHolderFragScreen, null, navOptions)
            }
        }
        ViewPagerAdapter(listOfImages)
        val tabLayout: TabLayout = binding.tabLayout
        val viewPager2 = binding.viewPager
        viewPager2.adapter = BleViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.Data)
                1 -> tab.text = getString(R.string.info)
            }

        }.attach()


    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothService.disConnect()
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(bluetoothReceiver)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(
                bluetoothReceiver, intentFilter, Context.RECEIVER_EXPORTED
            )

        } else {
            requireActivity().registerReceiver(bluetoothReceiver, intentFilter)

        }

    }
}