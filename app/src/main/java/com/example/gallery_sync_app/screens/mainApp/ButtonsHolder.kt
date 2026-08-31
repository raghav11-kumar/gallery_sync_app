package com.example.gallery_sync_app.screens.mainApp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentButtonsHolderBinding
import com.example.gallery_sync_app.screens.services.NotificationService
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import com.example.gallery_sync_app.screens.viewModels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ButtonsHolder.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ButtonsHolder : Fragment(R.layout.fragment_buttons_holder) {
    private lateinit var binding: FragmentButtonsHolderBinding
    private val authVm: AuthenticationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentButtonsHolderBinding.bind(view)
        authVm.getUser()
        super.onViewCreated(view, savedInstanceState)
        val bleBut = binding.bleButton
        val galleryBut = binding.GalleryButton
        val webBut = binding.webSocketButton
        val fmc_but = binding.fcmButton
        val userLogo=binding.userLogo
        val name=authVm.UserInformation
        viewLifecycleOwner.lifecycleScope.launch {
            authVm.UserInformation.collect { user ->
                user?.userName?.let { userName ->
                    if (userName.isNotEmpty()) {
                        userLogo.text = userName[0].uppercaseChar().toString()
                        Log.d("ButtonsHolder", "User name loaded: $userName")
                    }
                }
            }
        }
        userLogo.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(it,R.id.navigateMainToUserProfile)
        }

        bleBut.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(it, R.id.navigateMainToBle)
        }
        galleryBut.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(it, R.id.navigateMainToGallery)
        }
        webBut.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(it, R.id.navigateMainToWebSocket)

        }
        fmc_but.setOnClickListener {
            authVm.showPushNotification()
        }
    }

}