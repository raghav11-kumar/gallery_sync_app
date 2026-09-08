package com.example.gallery_sync_app.screens.Authentication

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallery_sync_app.screens.constants.UserStatus
import com.example.gallery_sync_app.screens.data.ImagBBResponse
import com.example.gallery_sync_app.screens.data.UserData
import com.example.gallery_sync_app.screens.data.local.LocalDataSaver
import com.example.gallery_sync_app.screens.repository.DataBaseRepository
import com.example.gallery_sync_app.screens.services.NotificationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
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
    //keeps track User Login Status
    private val isLoggedIn = MutableStateFlow(UserStatus.Unknown)
    val isIn = isLoggedIn.asStateFlow()

    private val errorFlow = MutableSharedFlow<String>()
    val errorFlowing = errorFlow.asSharedFlow()

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading = _isImageLoading.asStateFlow()

    private val _userId = MutableStateFlow(fbAuth.uid ?: "")
    //Gets The Data From Room by Flow .  When Changes Occur In Db   Automatically Updates ui

    val UserInformation = _userId.flatMapLatest { uid ->
        repo.getUser(uid)
    }

    private val imageInfo = MutableStateFlow<ImagBBResponse?>(null)
    val ImageInformation = imageInfo





    fun signIn(userName: String, userEmail: String, passWord: String) {
        viewModelScope.launch {
            try {
                // 1. Wait for Firebase to create the user
                val authResult =
                    fbAuth.createUserWithEmailAndPassword(userEmail.trim(), passWord.trim())
                        .await()
                val newUid = authResult.user?.uid ?: ""

                if (newUid.isNotEmpty()) {
                    // 2. Save to Firestore And In Local (Now we have the REAL Uid)
                    repo.saveUser(UserData(newUid, userName, userEmail))

                    localDataSaver.saveUser(userEmail)
                    _userId.value = newUid
                    isLoggedIn.value = UserStatus.Success

                    Log.e("AuthVM", "SuccessFul Sign In")
                }
            } catch (e: Exception) {
                isLoggedIn.value = UserStatus.Failure
                errorFlow.emit(e.message ?: "Failed Sign In")
                Log.e("AuthVM", "Failed Sign In ${e.message}")
            }
        }
    }

    //if The User already Signed In?can use login
    fun login(email: String, passWord: String) {
        viewModelScope.launch {
            try {
                fbAuth.signInWithEmailAndPassword(email, passWord).await()
                val newUid = fbAuth.uid ?: ""

                if (newUid.isNotEmpty()) {
                    localDataSaver.saveUser(email)
                    _userId.value = newUid
                    isLoggedIn.value = UserStatus.Success
                    repo.saveUserLocally(newUid)
                    Log.e("AuthVM", "SuccessFul Sign In")
                }

            } catch (e: Exception) {
                Log.e("AuthVm", "Failed To Login ${e.message}")
                isLoggedIn.value = UserStatus.Failure
                errorFlow.emit(e.message ?: "Failed To Login")
            }

        }

    }

    //checks Whether User is Active Or Not
    fun isUserActive(userEmail: String): Boolean {
        return localDataSaver.isUserActive(userEmail)

    }

    //gets the Notification payLoad From Server
    fun showPushNotification() {
        try {
            notification.showNotification("localMessage", "WassUp")
        } catch (e: Exception) {
            Log.e("FcmToken", "The Reason To Fail to get Token ${e.message}")
        }
    }



    //sends Image To ImgBB And gets The Response
    fun saveImage(uri: Uri) {
        val apiKey = "f06041a98c3e3556f51266c55a27e4b6"
        val multipartData = repo.convertUriToImage(uri)
        multipartData.onSuccess {
            viewModelScope.launch {
                _isImageLoading.value = true
                val response = repo.sendImage(
                    apiKey = apiKey, it
                )
                response.onSuccess {
                    Log.e("AuthVm", "SuccessFully Retrieved Image ${it}")
                    imageInfo.value = it
                    _isImageLoading.value = false
                }
                response.onFailure {
                    Log.e("AuthVm", "Failed TO Send IMage ${it.message}")
                    _isImageLoading.value = false
                    errorFlow.emit(it.message ?: "Failed to upload image")
                }

            }
        }.onFailure {
            viewModelScope.launch {
                errorFlow.emit(it.message ?: "Failed to process image")
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            repo.clearAllData()
            fbAuth.signOut()
            //clears the users Log State
            localDataSaver.clearUser()
            isLoggedIn.value = UserStatus.NotLogged
            _userId.value = ""
        }
    }
    fun updateUserName(name: String) {
        viewModelScope.launch {
            val result = repo.updateUserName(name)
            result.onFailure {
                errorFlow.emit(it.message ?: "Update Failed")
            }
        }
    }


}