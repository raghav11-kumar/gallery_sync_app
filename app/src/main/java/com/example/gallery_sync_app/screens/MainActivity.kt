package com.example.gallery_sync_app.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.screens.repo.Information

class MainActivity : AppCompatActivity() {
    private lateinit var info: Information
    override fun onCreate(savedInstanceState: Bundle?) {
        info = Information()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val b_id = findViewById<AppCompatButton>(R.id.button_one)
        val text_id = findViewById<AppCompatButton>(R.id.text_id)
        val list = info.getList()
        var i = 0
        text_id.text = list[i].song_name
        text_id.setOnClickListener {
            Log.e("MACT1", "text is clickable")
            Toast.makeText(this, "Clicking", Toast.LENGTH_LONG).show()
            val intent1 = Intent(this, MainActivity2::class.java)
            intent1.putExtra("index", i)
            startActivity(intent1)
        }
        b_id.setOnClickListener {
            i++
            if (i >= list.size)
                i = 0
            text_id.text = list[i].song_name
        }


    }
}