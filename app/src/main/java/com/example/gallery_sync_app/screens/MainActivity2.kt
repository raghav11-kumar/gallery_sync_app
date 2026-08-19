package com.example.gallery_sync_app.screens

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gallery_sync_app.R

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //where all the intializing happens and only executes once
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

    }

    override fun onResume() {
        super.onResume()
        //where the actvity is visble in foreground

    }

    override fun onPause() {
        //where the it loses focus
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        //this is where the user can see the activity
    }

    override fun onStop() {
        super.onStop()
        //the activity isnt destroyed its goone stop being visble to user
    }

    override fun onDestroy() {
        super.onDestroy()
        //where the activity is removed from memory
    }
}