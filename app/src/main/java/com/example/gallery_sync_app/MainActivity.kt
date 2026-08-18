package com.example.gallery_sync_app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val b_id=findViewById<Button>(R.id.button_one)
        val text_id=findViewById<TextView>(R.id.text_id)
        val list=listOf<String>(
            "payPhone",
            "Trees",
            "PaladinStrait",
            "cityWalls",
            "chlorine"
        )
        var i=0
        b_id.setOnClickListener {
            if(i==list.size)
                i=0
            text_id.text=list.get(i++)

        }



    }
}