package com.example.gallery_sync_app.screens.utils

import android.view.View
import androidx.navigation.findNavController

object ReusableFunctions {
      fun navigateSrcToDest(src: View,dest:Int){
        src.findNavController().navigate(dest)
    }
    fun areStringsEmpty(vararg text: String): Boolean{
        return text.any(){ it.isEmpty()}
    }
}