package com.example.gallery_sync_app.screens.mainApp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.gallery_sync_app.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginScreen : Fragment(R.layout.fragment_login_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton=view.findViewById<Button>(R.id.loginButton)
        val userName=view.findViewById<EditText>(R.id.userEmail)
       val context=view.context

        loginButton.setOnClickListener {
            Navigation.findNavController(it).navigate(
                R.id.navigateLoginToButtonHolder
            )
            Toast.makeText(context,"welcome ${userName.text}",Toast.LENGTH_LONG).show()
        }

    }

}