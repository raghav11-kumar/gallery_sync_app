package com.example.gallery_sync_app.screens.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.screens.GalleryViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel = GalleryViewModel()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
            //inflates the xml->view
        val b_id = findViewById<AppCompatButton>(R.id.button_one)
        val text_id = findViewById<AppCompatButton>(R.id.text_id)
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