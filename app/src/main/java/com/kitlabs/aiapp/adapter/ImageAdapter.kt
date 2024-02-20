package com.kitlabs.aiapp.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kitlabs.aiapp.databinding.ItemImagesBinding

class ImageAdapter(private val list: List<Bitmap>) : RecyclerView.Adapter<ImageAdapter.ImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = ItemImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            ivImage.setImageBitmap(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ImagesViewHolder(val binding: ItemImagesBinding): RecyclerView.ViewHolder(binding.root)
}
