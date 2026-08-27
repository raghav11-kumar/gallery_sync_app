package com.example.gallery_sync_app.screens.mainApp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.ActivityMainScreenBinding

class MainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}