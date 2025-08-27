package com.maximbircu.slide.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maximbircu.imageloader.api.Slide
import com.maximbircu.slide.R
import com.maximbircu.slide.domain.Image

class ImageListAdapter(
    private var images: List<Image>,
    private val onItemClick: (Image) -> Unit
) : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
        holder.itemView.setOnClickListener {
            onItemClick(image)
        }
    }

    override fun getItemCount(): Int = images.size

    fun updateImages(newImages: List<Image>) {
        images = newImages
        notifyDataSetChanged()
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleView: TextView = itemView.findViewById(R.id.titleTextView)
        val idTextView: TextView = itemView.findViewById(R.id.idTextView)

        fun bind(image: Image) {
            Slide.load(
                url = image.url,
                imageView = imageView,
                loadingPlaceholderResId = R.drawable.placeholder_loading,
                failurePlaceholderResId = R.drawable.placeholder_failure
            )
            titleView.text = image.title
            idTextView.text = image.id.toString()
        }
    }
}
