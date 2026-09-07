package com.example.gallery_sync_app.screens.Authentication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentSignInScreenBinding
import com.example.gallery_sync_app.screens.constants.DefaultValues
import com.example.gallery_sync_app.screens.constants.UserStatus
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import com.example.gallery_sync_app.screens.websockets.WebSocketsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInScreen : Fragment(R.layout.fragment_sign_in_screen) {
    lateinit var binding: FragmentSignInScreenBinding
     var webSocketsManager= WebSocketsManager()
    private val authVm: AuthenticationViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInScreenBinding.bind(view)
        val signInButton = binding.SignInButton
        val loginButton = binding.sigInLog

        viewLifecycleOwner.lifecycleScope.launch {
            authVm.errorFlow.collect { message ->
                ReusableFunctions.DefaultAlertDialog(
                    view.context, message, "OK", "Cancel"
                ) {}
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            authVm.isIn.collect { state ->
                when (state) {
                    UserStatus.Success -> {
                        view.findNavController().navigate(R.id.navigateSignInToButtonHolder)
                    }

                    UserStatus.Failure -> {
                        // Handled by errorFlow
                    }

                    else -> {
                    }
                }

            }
        }
        signInButton.setOnClickListener {
            webSocketsManager.sendMessage("Wassup")

            val email = binding.singInUserGmail.text.toString()
            val name = binding.signInUserName.text.toString()
            val pass = binding.signInUserPass.text.toString()

            if (ReusableFunctions.areStringsEmpty(name, email, pass)) {
                ReusableFunctions.DefaultAlertDialog(
                    view.context, "Fill The Email ,Name And PassWord", "Sure", "No",
                ) {}
            } else if (pass.length < 6) {
                ReusableFunctions.DefaultAlertDialog(view.context,"Password Must Be At Least 6 Characters","Sure","No"){
                //do some Action On positive Button Click
            }

            } else if (!email.matches(DefaultValues.emailRegex)) {
                ReusableFunctions.DefaultAlertDialog(view.context,"Please Enter Valid Email","Sure","No"){
                    //do some Action On positive Button Click
                }
            } else {
                authVm.signIn(name, email, pass)
            }
        }
        loginButton.setOnClickListener {
            view.findNavController().navigate(R.id.navigateSignInToLoginScreen)

        }

    }

}