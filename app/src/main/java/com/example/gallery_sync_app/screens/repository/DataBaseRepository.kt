package com.example.gallery_sync_app.screens.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.gallery_sync_app.screens.apis.ImageBBApi
import com.example.gallery_sync_app.screens.data.ImagBBResponse
import com.example.gallery_sync_app.screens.data.UserData
import com.example.gallery_sync_app.screens.data.roomDataBase.UserDao
import com.example.gallery_sync_app.screens.data.roomDataBase.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import javax.inject.Inject

class DataBaseRepository @Inject constructor(
   private val fbStore: FirebaseFirestore,
   private val fbAuth: FirebaseAuth,
    private val api: ImageBBApi,
   private val context: Context,
   private val localDB: UserDao
) {
    fun saveUser(userData: UserData) {
        val user = mapOf(
            "userName" to userData.userName,
            "uid" to userData.uid,
            "email" to userData.email
        )
        userData.uid?.let {
            fbStore.collection("users")
                .document(it)
                .set(user)
                .addOnSuccessListener {
                    Log.e("DataBaseRep", "Saved SuccessFully")
                }
                .addOnFailureListener {
                    Log.e("DataBaseRep", "Failed SuccessFully ${it.message}")
                }
        }

    }

    suspend fun getUser(uid: String): Users {
        val user = fbStore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                Log.e("DataBaseRep", "SuccessFully Got The User ${it}")
            }.addOnFailureListener {
                Log.e("DataBaseRep", "Failed To Get The User Cuz Of ${it.message}")

            }
            .await()

//        return user.toObject(UserData::class.java) // or toObject<UserData>()
        return localDB.getUser(uid)
    }

    private fun getCurrUid() = fbAuth.uid ?: ""
    suspend fun sendImage(
        apiKey: String,
        image: MultipartBody.Part
    ): Result<ImagBBResponse> {
        return try {
            val response = api.postImage(
                apiKey,
                image
            )
            fbStore.collection("users")
                .document(getCurrUid())
                .update("imageUrl", response.data.display_url)
                .addOnSuccessListener {
                    Log.e("DataBaseRep", "SuccessFully Added the ImageUrl")
                }
            localDB.updateImageUrl(getCurrUid(),response.data.display_url)
            Log.e("DataBaseRep", "The Retrival Of Image ${response}")
            Result.success(response)

        } catch (e: HttpException) {
            Result.failure(e)

        } catch (e1: Exception) {
            Result.failure(e1)
        }


    }

    fun convertUriToImage(uri: Uri): Result<MultipartBody.Part> {
        return try {
            val fileName = "upload_${System.currentTimeMillis()}.jpeg"
            val bytes = context.contentResolver
                .openInputStream(uri)
                ?.use { it.readBytes() }
                ?: throw IllegalStateException("Unable to read image")

            val requestBody = bytes.toRequestBody(
                "image/jpeg".toMediaType()
            )

            val multiPart = MultipartBody.Part.createFormData(
                "image",
                fileName,
                requestBody
            )
            Result.success(multiPart)

        } catch (e: Exception) {
            Log.e("AuthVm", "Failed To Convert IMage Cuz ${e.message}")
            Result.failure(e)
        }
    }
    suspend fun localSaveUser(userData: Users){
        try {
            localDB.InsertUser(userData)

        }catch (e: Exception){
            Log.e("DataBaseRep","Failed To Save The User lOcally ${e.message}")
        }
    }



}