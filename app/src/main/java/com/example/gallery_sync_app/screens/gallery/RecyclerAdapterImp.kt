package com.example.gallery_sync_app.screens.gallery

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentGalleryScreenBinding
import com.example.gallery_sync_app.screens.data.ImageInfo

class RecyclerAdapterImp(private val list: MutableList<ImageInfo>) : RecyclerView.Adapter<RecyclerAdapterImp.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
return list.size
    }

    open class ListViewHolder(view: View) : RecyclerView.ViewHolder(view)
}