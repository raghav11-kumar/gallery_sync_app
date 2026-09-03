package com.example.gallery_sync_app.screens.mainApp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentBLEDeviceBinding
import com.example.gallery_sync_app.screens.gallery.GalleryViewModel
import com.example.gallery_sync_app.screens.viewModels.AuthenticationViewModel
import kotlin.getValue

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BLEDevice.newInstance] factory method to
 * create an instance of this fragment.
 */
class BLEDevice : Fragment(R.layout.fragment_b_l_e_device){
    private val autVm: AuthenticationViewModel by activityViewModels()
    private lateinit var binding: FragmentBLEDeviceBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentBLEDeviceBinding.bind(view)
        (requireActivity() as AppCompatActivity)
            .supportActionBar
            ?.title = "Ble Device"
        val bleId=binding.bleText
        bleId.setOnClickListener {
            Log.e("BLE","is Clickable")

        }
    }


}