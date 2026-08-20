package com.example.gallery_sync_app.screens.fragments

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gallery_sync_app.R

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        val frq1_but=findViewById<Button>(R.id.frag_1)
        val frg2_but=findViewById<Button>(R.id.frag_2)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_main, fragment1())
                .commit()
        }
        frq1_but.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frag_main, fragment1())
                    .commit()
            }
        }
        frg2_but.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frag_main, fragment2())
                    .commit()
            }
        }



    }
}