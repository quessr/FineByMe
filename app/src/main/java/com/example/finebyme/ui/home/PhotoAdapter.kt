package com.example.finebyme.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finebyme.data.model.Photo
import com.example.finebyme.databinding.ItemPhotoBinding

class PhotoAdapter: RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private var photoList: List<Photo> = listOf()

    fun setPhoto(photos: List<Photo>) {
        this.photoList = photos
        notifyDataSetChanged()
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            Glide.with(binding.imageViewPhoto.context)
                .load(photo.urls.full)
                .into(binding.imageViewPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int = photoList.size
}