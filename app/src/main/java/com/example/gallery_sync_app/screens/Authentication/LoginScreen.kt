package com.example.gallery_sync_app.screens.Authentication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentLoginScreenBinding
import com.example.gallery_sync_app.screens.constants.UserStatus
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.fragment_login_screen) {
    private lateinit var binding: FragmentLoginScreenBinding
    private val authVm: AuthenticationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("Fragment", "In Login Fragment")
        binding = FragmentLoginScreenBinding.bind(view)

        super.onViewCreated(view, savedInstanceState)
        val loginButton = binding.loginButton

        viewLifecycleOwner.lifecycleScope.launch {
            authVm.isIn.collect { state ->
                when (state) {
                    UserStatus.Success -> {
                        ReusableFunctions.navigateSrcToDest(view, R.id.navigateLoginToButtonHolder)
                    }

                    UserStatus.Failure -> {
                        Toast.makeText(view.context, "Login Failed", Toast.LENGTH_SHORT).show()
                        // will Show  the ACTUAL reason from Firebase
                    }

                    UserStatus.NotLogged -> {
                        loginButton.isEnabled = true
                    }

                    else -> {

                    }
                }
            }
        }


        val context = view.context
        loginButton.setOnClickListener {
            val userEmail: String = binding.userEmail.text.toString()
            val passWord: String = binding.passInput.text.toString()
            Log.e("Fragment", "loginButton is clickable")
            if (ReusableFunctions.areStringsEmpty(passWord, userEmail)) {
                ReusableFunctions.DefaultAlertDialog(
                    context, "Fill The Email ,Name And PassWord", "Sure", "No",

                    ) {
                    //action that can be Performed By Clicking On Positive Button

                }
            } else {
                authVm.login(userEmail, passWord)
            }
        }
        val signInButton = binding.loginSigin
        signInButton.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(view, R.id.navigateLoginToSignIN)
        }
    }


}