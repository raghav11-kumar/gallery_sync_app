package com.example.gallery_sync_app.screens.utils

import android.Manifest
import android.R
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController

object ReusableFunctions {
    fun navigateSrcToDest(src: View, dest: Int) {
        src.findNavController().navigate(dest)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
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
    fun checkPermission(context: Context): Boolean{
        return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)== PackageManager.PERMISSION_GRANTED
    }
}