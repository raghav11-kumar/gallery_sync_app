package com.example.gallery_sync_app.screens.ble.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentBleInfoBinding
import com.example.gallery_sync_app.screens.ble.BluetoothService
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BleInfo : Fragment(R.layout.fragment_ble_info) {
    private lateinit var binding: FragmentBleInfoBinding

    @Inject
    lateinit var bluetoothService: BluetoothService

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
   ViewPagerAdapter(listOfImages)
        val tabLayout: TabLayout = binding.tabLayout
        val viewPager2 = binding.viewPager
        viewPager2.adapter = BleViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
when(position){
    0->tab.text=getString(R.string.Data)
    1->tab.text=getString(R.string.info)
}

        }.attach()


    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothService.disConnect()
    }
}