package com.example.gallery_sync_app.screens.repository

import android.util.Log
import com.example.gallery_sync_app.screens.data.userData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DataBaseRepository @Inject constructor(
    val fbStore: FirebaseFirestore
) {
    fun saveUser(userData: userData){
        val user=mapOf(
            "userName" to userData.userName,
            "uid" to userData.uid,
            "email" to userData.email
        )
        fbStore.collection("users")
            .document(userData.uid)
            .set(user)
            .addOnSuccessListener {
                Log.e("DataBaseRep","Saved SuccessFully")
            }
            .addOnFailureListener {
                Log.e("DataBaseRep","Failed SuccessFully ${it.message}")
            }

    }

}