package com.example.gallery_sync_app.screens.recyclerView

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.screens.data.GalleryImage
import com.example.gallery_sync_app.screens.data.ImageInfo

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
        var image_uri: Uri?=null
        val gal_image=registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ){uri->
            uri?.let {
                image_uri=it
            }
        }
        button_id.setOnClickListener {
            gal_image.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
            galleryList.add(
                GalleryImage(image_uri,"New GalleryImage")
            )
            adapter.notifyItemInserted(galleryList.size-1)


        }



    }
}