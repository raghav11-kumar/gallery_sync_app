package com.example.gallery_sync_app.screens.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject
import androidx.core.content.edit

class LocalDataSaver @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun saveUser(userEmail: String){
        sharedPreferences.edit {
            Log.e("LocalPref","Saved SuccessFully ${userEmail}")
            putString("userEmail",userEmail)
            putBoolean(userEmail, true)
        }

    }
    fun getUser()=sharedPreferences.getString("userEmail","")
    fun isUserActive(userEmail: String): Boolean{
       return sharedPreferences.getBoolean(userEmail,false)
    }



}