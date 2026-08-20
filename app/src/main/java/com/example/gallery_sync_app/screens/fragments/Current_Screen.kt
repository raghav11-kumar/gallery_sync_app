package com.example.gallery_sync_app.screens.fragments

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gallery_sync_app.R

class Current_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //this screen defines the flow of how in single activity the fragments are implemented
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_current_screen)
        val button1=findViewById<Button>(R.id.but_1)
        val button2=findViewById<Button>(R.id.but_2)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout3, fragment1())
                .commit()
        }
        button2.setOnClickListener {
            Log.e("Button","Button 2 CLick")
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayout3, fragment2())
                addToBackStack(null)
                commit()
            }
        }
        button1.setOnClickListener {
            Log.e("Button","Button 1 CLick")

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayout3, fragment1())
                addToBackStack(null)
                commit()
            }
        }

    }
}