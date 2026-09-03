package com.example.gallery_sync_app.screens.gallery

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.screens.data.ImageInfo
import com.example.gallery_sync_app.screens.data.Images
import com.example.gallery_sync_app.screens.utils.ReusableFunctions

class RecyclerAdapterImp(private var list: List<Images>,
    private val onDelete:(Images)-> Unit) :
    RecyclerView.Adapter<RecyclerAdapterImp.ListViewHolder>() {

    private var isEditMode = false

    fun updateList(newList: List<Images>) {
        list = newList
        notifyDataSetChanged()
    }

    fun setEditMode(enabled: Boolean) {
        isEditMode = enabled
        notifyDataSetChanged()
    }

    lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item_view, parent, false)
        context=parent.context
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        val image = list[position].url
        holder.imageTitleText.text=list[position].title
        holder.subTitleText.text=list[position].url

        holder.deleteId.visibility = if (isEditMode) View.VISIBLE else View.GONE

        holder.deleteId.setOnClickListener {
            ReusableFunctions.DefaultAlertDialog(context,"ARE YOU SURE YOU WANT TO DELETE","YES","NO") {
                onDelete(list[position])
            }
        }

        holder.progressBar.visibility = View.VISIBLE

        Glide.with(holder.itemView.context)
            .load(image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.
                        VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }
            })
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
        val progressBar = view.findViewById<ProgressBar>(R.id.imageProgressBar)
    }
}
