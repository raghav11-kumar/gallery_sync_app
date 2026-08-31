package com.example.gallery_sync_app.screens.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery_sync_app.screens.constants.UserStatus
import com.example.gallery_sync_app.screens.data.local.LocalDataSaver
import com.example.gallery_sync_app.screens.services.NotificationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val fbAuth: FirebaseAuth,
    private val localDataSaver: LocalDataSaver,
    private val fcm: FirebaseMessaging,
    private val notification: NotificationService
) : ViewModel() {
    private val isLoggedIn = MutableStateFlow<UserStatus>(UserStatus.Unknown)
    val isIn = isLoggedIn.asStateFlow()

    fun signIn(userName: String, passWord: String) {
        viewModelScope.launch {
            fbAuth.createUserWithEmailAndPassword(userName.trim(), passWord.trim())
                .addOnSuccessListener {


                    isLoggedIn.value = UserStatus.Success
                    localDataSaver.saveUser(userName)
                    Log.e("AuthVM", "SuccessFul Sign In")
                }.addOnFailureListener {
                    isLoggedIn.value = UserStatus.Failure
                    Log.e("AuthVM", "Failed Sign In")
                }
        }
    }

    fun isUserActive(userEmail: String): Boolean {
        return localDataSaver.isUserActive(userEmail)

    }

    fun showPushNotification() {
        try {
            notification.showNotification("localMessage", "WassUp")
        } catch (e: Exception) {
            Log.e("FcmToken", "The Reason To Fail to get Token ${e.message}")
        }
    }
}