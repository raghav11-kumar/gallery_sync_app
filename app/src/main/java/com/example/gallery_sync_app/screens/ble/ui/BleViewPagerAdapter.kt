package com.example.gallery_sync_app.screens.ble.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BleViewPagerAdapter(fragment: Fragment)  : FragmentStateAdapter(fragment) {

    // Number of tabs/fragments you want to display
    override fun getItemCount(): Int = 2

    // Return the specific Fragment instance for each tab position
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0-> DataLayout()
            1-> InfoLayout()
            else  -> DataLayout()
        }
    }
}