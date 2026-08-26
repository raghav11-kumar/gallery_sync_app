package com.example.gallery_sync_app.screens.recyclerView

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.R
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_sync_app.screens.data.GalleryImage
import com.example.gallery_sync_app.screens.data.ImageInfo

class RecyclerAdapter(private val galleryList:List<GalleryImage>):
    RecyclerView.Adapter<RecyclerAdapter.GalleryAdapter>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryAdapter {
        val view= LayoutInflater.from(parent.context).inflate(com.example.gallery_sync_app.R.layout.item_view,parent,false)
        return GalleryAdapter(view)
    }

    override fun onBindViewHolder(
        holder: GalleryAdapter,
        position: Int
    ) {
        holder.title_id.text=galleryList[position].title
        holder.image_id.setImageURI(galleryList[position].image_Url)
    }

    override fun getItemCount(): Int {
      return  galleryList.size
    }




    inner class GalleryAdapter(itemView: View): RecyclerView.ViewHolder(itemView){
        val title_id=itemView.findViewById<TextView>(com.example.gallery_sync_app.R.id.viewText_id)
        val image_id=itemView.findViewById<ImageView>(com.example.gallery_sync_app.R.id.image_id)

    }


}