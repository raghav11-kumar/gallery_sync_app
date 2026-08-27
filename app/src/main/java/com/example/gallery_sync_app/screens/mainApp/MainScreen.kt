package com.example.gallery_sync_app.screens.mainApp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gallery_sync_app.databinding.ActivityMainScreenBinding
import com.example.gallery_sync_app.screens.viewModels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    private val authVm: AuthenticationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}