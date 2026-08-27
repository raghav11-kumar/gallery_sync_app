package com.example.gallery_sync_app.screens.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel  @Inject constructor(
    private val fbAuth: FirebaseAuth
): ViewModel() {
    fun signIn(){
        viewModelScope.launch {
            fbAuth.createUserWithEmailAndPassword("a@gmail.com","abc@1209")
                .addOnSuccessListener {
                    Log.e("AuthVM","SuccessFul Sign In")
                }.addOnFailureListener {
                    Log.e("AuthVM","Failed Sign In")

                }
        }

    }


}