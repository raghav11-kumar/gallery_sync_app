package com.example.gallery_sync_app.screens.viewModels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery_sync_app.screens.constants.UserStatus
import com.example.gallery_sync_app.screens.data.local.LocalDataSaver
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel  @Inject constructor(
    private val fbAuth: FirebaseAuth,
    private val localDataSaver: LocalDataSaver
): ViewModel() {
    private val isLoggedIn= MutableStateFlow<UserStatus>(UserStatus.Unknown)
    val isIn=isLoggedIn.asStateFlow()

    fun signIn(userName:String,passWord: String){
        viewModelScope.launch {
            fbAuth.createUserWithEmailAndPassword(userName.trim(),passWord.trim())
                .addOnSuccessListener {
                    isLoggedIn.value= UserStatus.Success
                    localDataSaver.saveUser(userName)
                    Log.e("AuthVM","SuccessFul Sign In")
                }.addOnFailureListener {
                    isLoggedIn.value= UserStatus.Failure
                    Log.e("AuthVM","Failed Sign In")
                }
        }
    }
    fun isUserActive(userEmail: String): Boolean{
        return localDataSaver.isUserActive(userEmail)

    }
}