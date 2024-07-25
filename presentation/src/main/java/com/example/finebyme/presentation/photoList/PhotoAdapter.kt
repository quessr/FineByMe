package com.example.finebyme.presentation.photoList

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finebyme.domain.entity.Photo
import com.example.finebyme.domain.interaction.OnPhotoClickListener
import com.example.finebyme.presentation.base.BaseViewModel
import com.example.finebyme.presentation.databinding.ItemPhotoBinding
import com.example.finebyme.presentation.utils.ImageLoader

class PhotoAdapter(
    private val viewModel: BaseViewModel,
) :
    ListAdapter<Photo, PhotoAdapter.PhotoViewHolder>(diffUtil) {

    private var onPhotoClickListener: OnPhotoClickListener? = null
//
    fun setOnPhotoClickListener(listener: OnPhotoClickListener) {
        this.onPhotoClickListener = listener
    }

//    interface OnPhotoClickListener {
//        fun onPhotoClick(photo: Photo)
//    }

    class PhotoViewHolder(
        private val binding: ItemPhotoBinding,
        private val onPhotoClickListener: OnPhotoClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo, height: Int) {
            binding.imageViewPhoto.setImageDrawable(null)
            // 뷰의 높이 설정
            binding.imageViewPhoto.layoutParams.height = height

            ImageLoader.loadImage(
                context = binding.imageViewPhoto.context,
                url = photo.thumbUrl,
                imageView = binding.imageViewPhoto
            )

            binding.imageViewPhoto.scaleType = ImageView.ScaleType.CENTER_CROP

            // 사진 클릭 이벤트 설정
            binding.imageViewPhoto.setOnClickListener {
                Log.d("PhotoAdapter","imageViewPhoto.setOnClickListener")
                onPhotoClickListener.onPhotoClick(photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding, onPhotoClickListener!!)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val height = viewModel.getPhotoHeight(position)

        // dp를 px로 변환
        val scale = holder.itemView.context.resources.displayMetrics.density
        val heightInPx = (height * scale + 0.5f).toInt()


        holder.bind(getItem(position), heightInPx)
    }

    fun clearData() {
        submitList(emptyList())
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }
}