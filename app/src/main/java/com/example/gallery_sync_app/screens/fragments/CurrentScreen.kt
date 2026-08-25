package com.example.gallery_sync_app.screens.fragments

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gallery_sync_app.R

class CurrentScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //this screen defines the flow of how in single activity the fragments are implemented
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_current_screen)
        val button1=findViewById<Button>(R.id.but_1)
        val button2=findViewById<Button>(R.id.but_2)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout3, Fragment1())
                .commit()
        }
        button2.setOnClickListener {
            Log.e("Button","Button 2 CLick")
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayout3, Fragment2())
                addToBackStack(null)
                commit()
            }
        }
        button1.setOnClickListener {
            Log.e("Button","Button 1 CLick")

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayout3, Fragment1())
                addToBackStack(null)
                commit()
            }
        }

    }
}