package com.example.gallery_sync_app.screens.screens

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.screens.repo.Information

class MainActivity2 : AppCompatActivity() {
    private lateinit var info: Information


    override fun onCreate(savedInstanceState: Bundle?) {
        //where all the intializing happens and only executes once
        info = Information()
        val list = info.getList()
        val index = intent.getIntExtra("index", 0)
        super.onCreate(savedInstanceState)
        val song = list[index]
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        val image_id = findViewById<ImageView>(R.id.image)
        image_id.setImageResource(song.image)
        val artist_text = findViewById<TextView>(R.id.artist)
        artist_text.text = song.song_name
        val bio = findViewById<TextView>(R.id.bio)
        bio.text = song.bio
    }

    override fun onResume() {
        super.onResume()
        //where the actvity is visble in foreground

    }

    override fun onPause() {
        //where  it loses focus
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