package com.example.gallery_sync_app.screens.gallery

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.screens.data.ImageInfo
import com.example.gallery_sync_app.screens.data.Images

class RecyclerAdapterImp(private var list: List<Images>,
    private val onDelete:(Images)-> Unit) :
    RecyclerView.Adapter<RecyclerAdapterImp.ListViewHolder>() {

    fun updateList(newList: List<Images>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item_view, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        val image = list[position].url
        holder.imageTitleText.text=list[position].title
        holder.subTitleText.text=list[position].url
        holder.deleteId.setOnClickListener {
            onDelete(list[position])

        }

        Glide.with(holder.itemView.context)
            .load(image)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .centerCrop()
            .into(holder.imageId)
    }

    override fun getItemCount(): Int = list.size

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageId = view.findViewById<ImageView>(R.id.recImage)
        val imageTitleText=view.findViewById<TextView>(R.id.imageTitleText)
        val subTitleText=view.findViewById<TextView>(R.id.imageSubtitleText)
        val deleteId = view.findViewById<ImageView>(R.id.deleteIcon)
    }
}
