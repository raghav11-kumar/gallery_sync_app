package com.example.gallery_sync_app.screens.utils

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController

object ReusableFunctions {
      fun navigateSrcToDest(src: View,dest:Int){
        src.findNavController().navigate(dest)
    }
    fun areStringsEmpty(vararg text: String): Boolean{
        return text.any { it.isEmpty()}
    }
    fun DefaultAlertDialog(context: Context,message: String,pos: String,neg: String,onDelete:()-> Unit): AlertDialog{
        Log.e("REUSABLEFUN","ITS CALLED")
        val alertDi= AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(pos
            ) { _, _ ->
                onDelete()
                Log.e("REUSABLEFUN","ITS CALLED")

            }
            .setNegativeButton(neg) { _, _ ->
                Log.e("REUSABLEFUNS","neg CAlled")
            }.show()



        return  alertDi
    }
}