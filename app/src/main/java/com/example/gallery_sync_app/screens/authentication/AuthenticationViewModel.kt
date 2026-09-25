package com.example.gallery_sync_app.screens.authentication

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
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val TAG: String = "AUTHVM"

    private val isLoggedIn = MutableStateFlow(UserStatus.Unknown)
    val isIn = isLoggedIn.asStateFlow()

    private val errorFlow = MutableSharedFlow<String>()
    val errorFlowing = errorFlow.asSharedFlow()

    private val isLoadingState = MutableStateFlow(false)
    val isLoading = isLoadingState.asStateFlow()

    private val isImageLoadingState = MutableStateFlow(false)
    val isImageLoading = isImageLoadingState.asStateFlow()

    private val userId = MutableStateFlow(fbAuth.uid ?: "")
    //Gets The Data From Room by Flow .  When Changes Occur In Db   Automatically Updates ui

    @OptIn(ExperimentalCoroutinesApi::class)
    val userInformation = userId.flatMapLatest { uid ->
        repo.getUser(uid)
    }

    private val imageInfo = MutableStateFlow<ImagBBResponse?>(null)


    fun signIn(userName: String, userEmail: String, passWord: String) {
        viewModelScope.launch {
            isLoadingState.value = true
            try {
                // 1. Wait for Firebase to create the user
                val authResult =
                    fbAuth.createUserWithEmailAndPassword(userEmail.trim(), passWord.trim()).await()
                val newUid = authResult.user?.uid ?: ""

                if (newUid.isNotEmpty()) {
                    // 2. Save to Firestore And In Local (Now we have the REAL Uid)
                    repo.saveUser(UserData(newUid, userName, userEmail))

                    localDataSaver.saveUser(userEmail)
                    userId.value = newUid
                    isLoggedIn.value = UserStatus.Success

                    Log.e(TAG, "SuccessFul Sign In")
                }
            } catch (e: Exception) {
                isLoggedIn.value = UserStatus.Failure
                errorFlow.emit(e.message ?: "Failed Sign In")
                Log.e(TAG, "Failed Sign In ${e.message}")
            } finally {
                isLoadingState.value = false
            }
        }
    }

    //if The User already Signed In?can use login
    fun login(email: String, passWord: String) {
        viewModelScope.launch {
            isLoadingState.value = true
            try {
                fbAuth.signInWithEmailAndPassword(email, passWord).await()
                val newUid = fbAuth.uid ?: ""

                if (newUid.isNotEmpty()) {
                    localDataSaver.saveUser(email)
                    userId.value = newUid
                    isLoggedIn.value = UserStatus.Success
                    repo.saveUserLocally(newUid)
                    Log.e(TAG, "SuccessFul Sign In")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Failed To Login ${e.message}")
                isLoggedIn.value = UserStatus.Failure
                errorFlow.emit(e.message ?: "Failed To Login")
            } finally {
                isLoadingState.value = false
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
                isImageLoadingState.value = true
                val response = repo.sendImage(
                    apiKey = apiKey, it
                )
                response.onSuccess {
                    Log.e(TAG, "SuccessFully Retrieved Image ${it}")
                    imageInfo.value = it
                    isImageLoadingState.value = false
                }
                response.onFailure {
                    Log.e(TAG, "Failed TO Send IMage ${it.message}")
                    isImageLoadingState.value = false
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
            isLoadingState.value = true
            try {
                repo.clearAllData()
                fbAuth.signOut()
                //clears the users Log State
                localDataSaver.clearUser()
                isLoggedIn.value = UserStatus.NotLogged
                userId.value = ""
            } finally {
                isLoadingState.value = false
            }
        }
    }

    fun updateUserName(name: String) {
        viewModelScope.launch {
            isLoadingState.value = true
            try {
                val result = repo.updateUserName(name)
                result.onFailure {
                    errorFlow.emit(it.message ?: "Update Failed")
                }
                result.onSuccess {
                    Log.d(TAG, "SuccessFully Updated UserName")
                }
            } finally {
                isLoadingState.value = false
            }
        }
    }


}