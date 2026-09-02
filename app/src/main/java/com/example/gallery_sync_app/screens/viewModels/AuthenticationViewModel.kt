package com.example.gallery_sync_app.screens.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery_sync_app.screens.constants.UserStatus
import com.example.gallery_sync_app.screens.data.ImagBBResponse
import com.example.gallery_sync_app.screens.data.UserData
import com.example.gallery_sync_app.screens.data.local.LocalDataSaver
import com.example.gallery_sync_app.screens.data.roomDataBase.Users
import com.example.gallery_sync_app.screens.repository.DataBaseRepository
import com.example.gallery_sync_app.screens.services.NotificationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val fbAuth: FirebaseAuth,
    private val localDataSaver: LocalDataSaver,
    private val fcm: FirebaseMessaging,
    private val repo: DataBaseRepository,
    private val notification: NotificationService,

    ) : ViewModel() {
    private val isLoggedIn = MutableStateFlow<UserStatus>(UserStatus.Unknown)
    val isIn = isLoggedIn.asStateFlow()
    private fun CurrUserUid() = fbAuth.uid ?: ""


    fun signIn(userName: String, userEmail: String, passWord: String) {
        viewModelScope.launch {
            try {
                // 1. Wait for Firebase to create the user
                val authResult =
                    fbAuth.createUserWithEmailAndPassword(userEmail.trim(), passWord.trim()).await()
                val newUid = authResult.user?.uid ?: ""

                if (newUid.isNotEmpty()) {
                    // 2. Save to Firestore (Now we have the REAL Uid)
                    repo.saveUser(UserData(newUid, userName, userEmail))

                    // 3. Save to Local Room DB
                    repo.localSaveUser(
                        Users(
                            userUid = newUid, name = userName, email = userEmail, imageUrl = ""
                        )
                    )

                    localDataSaver.saveUser(userEmail)
                    repo.syncRoomToFireStore(uid = newUid)
                    isLoggedIn.value = UserStatus.Success

                    Log.e("AuthVM", "SuccessFul Sign In")
                }
            } catch (e: Exception) {
                isLoggedIn.value = UserStatus.Failure
                Log.e("AuthVM", "Failed Sign In ${e.message}")
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


    //Gets The Data From Room by Flow .  When Changes Occur In Db   Automatically Updates ui
    val UserInformation = repo.getUser(uid = CurrUserUid())



    private val imageInfo = MutableStateFlow<ImagBBResponse?>(null)
    val ImageInformation = imageInfo
    fun saveImage(uri: Uri) {
        val apiKey = "f06041a98c3e3556f51266c55a27e4b6"
        val multipartData = repo.convertUriToImage(uri)
        multipartData.onSuccess {
            viewModelScope.launch {
                val response = repo.sendImage(
                    apiKey = apiKey, it
                )
                response.onSuccess {
                    Log.e("AuthVm", "SuccessFully Retrieved Image ${it}")
                    imageInfo.value = it
                }
                response.onFailure {
                    Log.e("AuthVm", "Failed TO Send IMage ${it.message}")
                }

            }
        }
    }


}