package com.example.gallery_sync_app.screens.utils

import android.R
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController

object ReusableFunctions {
    fun navigateSrcToDest(src: View, dest: Int) {
        src.findNavController().navigate(dest)
    }

    fun areStringsEmpty(vararg text: String): Boolean {
        return text.any { it.isEmpty() }
    }

    fun DefaultAlertDialog(
        context: Context, message: String, pos: String, neg: String, onDelete: () -> Unit
    ): AlertDialog {
val customView= LayoutInflater.from(context)
    .inflate(com.example.gallery_sync_app.R.layout.custom_alert_dialog
        , null,false)
        val posBut=customView.findViewById<Button>(com.example.gallery_sync_app.R.id.btnPositive)
        val negBut=customView.findViewById<Button>(com.example.gallery_sync_app.R.id.btnNegative)
        val heading=customView.findViewById<TextView>(com.example.gallery_sync_app.R.id.connection)

        Log.e("REUSABLEFUN", "ITS CALLED")
        heading.text = message

        val alertDi = AlertDialog.Builder(context)
            .setView(customView)
            .show()
        posBut.setOnClickListener {
            onDelete()
            alertDi.dismiss()
        }
        negBut.setOnClickListener {
            alertDi.dismiss()
        }



        return alertDi
    }
}