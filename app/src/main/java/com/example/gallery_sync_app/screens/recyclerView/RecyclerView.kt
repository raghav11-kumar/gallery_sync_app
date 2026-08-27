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
import com.example.gallery_sync_app.databinding.ActivityMainBinding
import com.example.gallery_sync_app.databinding.ActivityRecyclerViewBinding
import com.example.gallery_sync_app.screens.data.GalleryImage

class RecyclerView : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val galleryList=mutableListOf<GalleryImage>()
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter= RecyclerAdapter(galleryList)
        val recy_id=binding.recyId
        recy_id.adapter=adapter
        recy_id.layoutManager= LinearLayoutManager(this)
        val button_id=binding.add
        val edit_text=binding.etId
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