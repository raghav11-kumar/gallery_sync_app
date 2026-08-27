package com.example.gallery_sync_app.screens.mainApp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentLoginScreenBinding
import com.example.gallery_sync_app.screens.viewModels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

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
    private  val authVm: AuthenticationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("Fragment","In Login Fragment")
        binding= FragmentLoginScreenBinding.bind(view)

        super.onViewCreated(view, savedInstanceState)
        val loginButton=binding.loginButton
        val userName=binding.userEmail
       val context=view.context

        loginButton.setOnClickListener {
            Log.e("Fragment","loginButton is clickable")
            authVm.signIn()
            Navigation.findNavController(it).navigate(
                R.id.navigateLoginToButtonHolder
            )
            Toast.makeText(context,"welcome ${userName.text}",Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}