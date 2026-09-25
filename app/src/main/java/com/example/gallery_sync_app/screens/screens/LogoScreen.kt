package com.example.gallery_sync_app.screens.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentLoginScreenBinding
<<<<<<< HEAD
import com.example.gallery_sync_app.screens.data.local.LocalDataSaver
import com.example.gallery_sync_app.screens.authentication.AuthenticationViewModel
=======
import com.example.gallery_sync_app.screens.Authentication.AuthenticationViewModel
import com.example.gallery_sync_app.screens.data.local.LocalDataSaver
>>>>>>> 68cd230654625a7bfe4f3923229c9c5188cc6e5f
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class LogoScreen : Fragment(R.layout.fragment_logo_screen) {
    private val authVm: AuthenticationViewModel by viewModels()

    @Inject
    lateinit var localDataSaver: LocalDataSaver

    private lateinit var binding: FragmentLoginScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userEmail = localDataSaver.getUser() ?: ""
        val isAlreadyIn = authVm.isUserActive(userEmail)
        binding = FragmentLoginScreenBinding.inflate(layoutInflater)
        lifecycleScope.launch {
            delay(3000.milliseconds)
            if (isAlreadyIn) {
                view.findNavController().navigate(R.id.navigateLogoToMainScreen)
            } else {
                view.findNavController().navigate(R.id.navigateLogoToLogin)
            }
        }

    }
}