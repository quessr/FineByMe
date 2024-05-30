package com.example.finebyme.ui.photoList

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
    private var heights = mutableMapOf<Int, Int>()

    fun setPhoto(photos: List<Photo>) {
        this.photoList = photos
        heights.clear() // 사진 목록이 업데이트될 때 높이 정보 초기화
        notifyDataSetChanged()
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo, height: Int) {
            // 이미지 초기화
            Glide.with(binding.imageViewPhoto.context).clear(binding.imageViewPhoto)
            binding.imageViewPhoto.setImageDrawable(null)

            Glide.with(binding.imageViewPhoto.context)
                .load(photo.urls.thumb)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageViewPhoto)

            binding.imageViewPhoto.scaleType = ImageView.ScaleType.CENTER_CROP

            // 뷰의 높이 설정
            val layoutParams = binding.root.layoutParams
            layoutParams.height = height
            binding.root.layoutParams = layoutParams
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val minHeight = 100 // dp
        val maxHeight = 300 // dp
        val randomHeight: Int = if (heights.containsKey(position)) {
            heights[position]!!
        } else {
            Random().nextInt(maxHeight - minHeight + 1) + minHeight
        }
        heights[position] = randomHeight // 높이 저장

        // dp를 px로 변환
        val scale = holder.itemView.context.resources.displayMetrics.density
        val heightInPx = (randomHeight * scale + 0.5f).toInt()

        holder.bind(photoList[position], heightInPx)
    }

    override fun getItemCount(): Int = photoList.size

    fun clearData() {
        photoList = listOf()
        notifyDataSetChanged()
    }
}