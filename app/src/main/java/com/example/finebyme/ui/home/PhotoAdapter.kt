package com.example.finebyme.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.finebyme.data.model.Photo
import com.example.finebyme.databinding.ItemPhotoBinding
import java.util.Random

class PhotoAdapter: RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private var photoList: List<Photo> = listOf()

    fun setPhoto(photos: List<Photo>) {
        this.photoList = photos
        notifyDataSetChanged()
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            // 이미지 초기화
            Glide.with(binding.imageViewPhoto.context).clear(binding.imageViewPhoto)
            binding.imageViewPhoto.setImageDrawable(null)

            Glide.with(binding.imageViewPhoto.context)
                .load(photo.urls.thumb)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageViewPhoto)

            binding.imageViewPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])

        // 랜덤 높이 생성 (예: 100dp에서 300dp 사이)
        val minHeight = 100 // dp
        val maxHeight = 300 // dp
        val randomHeight: Int = Random().nextInt(maxHeight - minHeight + 1) + minHeight

        // dp를 px로 변환
        val scale = holder.itemView.context.resources.displayMetrics.density
        val heightInPx = (randomHeight * scale + 0.5f).toInt()

        // 높이 설정
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = heightInPx
        holder.itemView.layoutParams = layoutParams
    }

    override fun getItemCount(): Int = photoList.size

    fun clearData() {
        photoList = listOf()
        notifyDataSetChanged()
    }
}