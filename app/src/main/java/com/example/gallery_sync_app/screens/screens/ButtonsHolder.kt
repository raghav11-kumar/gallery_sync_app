package com.example.gallery_sync_app.screens.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentButtonsHolderBinding
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import com.example.gallery_sync_app.screens.Authentication.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ButtonsHolder : Fragment(R.layout.fragment_buttons_holder) {
    private lateinit var binding: FragmentButtonsHolderBinding
    private val authVm: AuthenticationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentButtonsHolderBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        val bleBut = binding.bleButton
        val galleryBut = binding.GalleryButton
        val webBut = binding.webSocketButton
        val fmcBut = binding.fcmButton
        bleBut.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(it, R.id.navigateMainToBle)
        }
        galleryBut.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(it, R.id.navigateMainToGallery)
        }
        webBut.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(it, R.id.navigateMainToWebSocket)

        }
        fmcBut.setOnClickListener {
            authVm.showPushNotification()
        }
        binding.mqqtButton.visibility= View.GONE
    }

}