package com.example.gallery_sync_app.screens.recyclerView

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.screens.data.GalleryImage

class RecyclerView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val galleryList=mutableListOf<GalleryImage>()
        setContentView(R.layout.activity_recycler_view)

        val adapter= RecyclerAdapter(galleryList)
        val recy_id=findViewById<RecyclerView>(R.id.recy_id)
        recy_id.adapter=adapter
        recy_id.layoutManager= LinearLayoutManager(this)
        val button_id=findViewById<Button>(R.id.add)
        val edit_text=findViewById<EditText>(R.id.et_id)

        val gal_image=registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ){uri->
            uri?.let {

                galleryList.add(
                    GalleryImage(it,"${edit_text.text}")
                )
                adapter.notifyItemInserted(galleryList.size-1)
            }
        }
        button_id.setOnClickListener {
            gal_image.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )




        }



    }
}