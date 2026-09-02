package com.example.gallery_sync_app.screens.mainApp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentLoginScreenBinding
import com.example.gallery_sync_app.screens.data.local.LocalDataSaver
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import com.example.gallery_sync_app.screens.viewModels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogoScreen : Fragment(R.layout.fragment_logo_screen) {
    private val authVm: AuthenticationViewModel by viewModels()
    @Inject
    lateinit var localDataSaver: LocalDataSaver

    private lateinit var binding: FragmentLoginScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userEmail=localDataSaver.getUser()?:""
        val isAlreadyIn=authVm.isUserActive(userEmail)
        binding= FragmentLoginScreenBinding.inflate(layoutInflater)
        Handler(Looper.getMainLooper()).postDelayed({
            if(!isAlreadyIn)ReusableFunctions.navigateSrcToDest(view,R.id.navigateLogoToLogin)
            else ReusableFunctions.navigateSrcToDest(view,R.id.navigateLogoToMainScreen)
        },2000)


    }

}