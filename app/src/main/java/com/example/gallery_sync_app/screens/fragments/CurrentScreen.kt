package com.example.gallery_sync_app.screens.fragments

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.ActivityCurrentScreenBinding

class CurrentScreen : AppCompatActivity() {
    private lateinit var binding: ActivityCurrentScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        //this screen defines the flow of how in single activity the fragments are implemented
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityCurrentScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val button1=binding.but1
        val button2=binding.but2
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