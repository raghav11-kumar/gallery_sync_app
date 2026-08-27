package com.example.gallery_sync_app.screens.intents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.ActivityMainBinding
import com.example.gallery_sync_app.screens.GalleryViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel = GalleryViewModel()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
            //inflates the xml->view
        val b_id = binding.buttonOne
        val text_id =binding.textId
        val list = viewModel.getList()
        var i = 0
        text_id.text = list[i].song_name
        text_id.setOnClickListener {
            Log.e("MACT1", "text is clickable")
            val intent1 = Intent(this, MainActivity2::class.java)
            intent1.putExtra("index", i)
            startActivity(intent1)
        }
        b_id.setOnClickListener {
            i++
            if (i >= list.size) i = 0
            text_id.text = list[i].song_name
        }


    }
}