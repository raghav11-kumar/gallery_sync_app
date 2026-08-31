package com.example.gallery_sync_app.screens.repository

import android.util.Log
import com.example.gallery_sync_app.screens.data.userData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DataBaseRepository @Inject constructor(
    val fbStore: FirebaseFirestore
) {
    fun saveUser(userData: userData){
        val user=mapOf(
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
    suspend fun getUser(uid: String): userData? {
        val user = fbStore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                Log.e("DataBaseRep","SuccessFully Got The User ${it}")
            }.addOnFailureListener {
                Log.e("DataBaseRep","Failed To Get The User Cuz Of ${it.message}")

            }
            .await()

        return user.toObject(userData::class.java) // or toObject<userData>()
    }

}