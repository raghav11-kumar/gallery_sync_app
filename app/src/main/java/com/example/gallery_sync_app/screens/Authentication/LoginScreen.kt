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
import com.example.gallery_sync_app.screens.constants.DefaultValues
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
                        ReusableFunctions.navigateSrcToDest(view, R.id.navigateLoginToButtonHolder)
                    }

                    UserStatus.Failure -> {
                        // Handled by errorFlow
                    }

                    UserStatus.NotLogged -> {
                        loginButton.isEnabled = true
                    }

                    else -> {

                    }
                }
            }
        }


        loginButton.setOnClickListener {
            val userEmail: String = binding.userEmail.text.toString()
            val passWord: String = binding.passInput.text.toString()
            Log.e("Fragment", "loginButton is clickable")
            if (ReusableFunctions.areStringsEmpty(userEmail, passWord)) {
                ReusableFunctions.DefaultAlertDialog(
                    view.context, "Fill The Email ,Name And PassWord", "Sure", "No",
                ) {}
            } else if (passWord.length < 6) {
                ReusableFunctions.DefaultAlertDialog(view.context,"Password Must Be At Least 6 Characters","Sure","No"){
                    //do some Action On positive Button Click
                }
            } else if (!userEmail.matches(DefaultValues.emailRegex)) {
                ReusableFunctions.DefaultAlertDialog(view.context,"Please Enter Valid Email","Sure","No"){
                    //do some Action On positive Button Click
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