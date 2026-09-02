package com.example.gallery_sync_app.screens.mainApp

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
import com.example.gallery_sync_app.screens.viewModels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
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
                        // Show the ACTUAL reason from Firebase
                    }

                    else -> {
                    }
                }
            }
        }


        val context = view.context
        loginButton.setOnClickListener {
            val userName:String=binding.nameCred.text.toString()
            val userEmail: String = binding.userEmail.text.toString()
            val passWord: String = binding.passInput.text.toString()
            Log.e("Fragment", "loginButton is clickable")
            if (ReusableFunctions.areStringsEmpty(userName, passWord,userEmail)) {
                ReusableFunctions.DefaultAlertDialog(
                    context, "Fill The Email ,Name And PassWord", "Sure", "No"
                ).show()
            } else {
                authVm.signIn(userName,userEmail, passWord)
            }
        }
    }


}