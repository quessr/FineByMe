package com.example.finebyme.ui.photoList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.finebyme.data.db.Photo
import com.example.finebyme.data.model.UnsplashPhoto
import com.example.finebyme.databinding.ItemPhotoBinding
import com.example.finebyme.ui.base.BaseViewModel
import java.util.Random

class PhotoAdapter(private val viewModel: BaseViewModel): RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private var photoList: List<Photo> = listOf()
    private var listener: OnPhotoClickListener? = null


    fun setPhoto(favoritePhotoList: List<Photo>) {
        this.photoList = favoritePhotoList
        notifyDataSetChanged()
    }

    interface OnPhotoClickListener {
        fun onPhotoClick(photo: Photo)
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding, private val listener: OnPhotoClickListener?): RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo, height: Int) {
            // 이미지 초기화
            Glide.with(binding.imageViewPhoto.context).clear(binding.imageViewPhoto)
            binding.imageViewPhoto.setImageDrawable(null)

            // 뷰의 높이 설정
            binding.imageViewPhoto.layoutParams.height = height

            Glide.with(binding.imageViewPhoto.context)
                .load(photo.thumbUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageViewPhoto)

            binding.imageViewPhoto.scaleType = ImageView.ScaleType.CENTER_CROP

            // 사진 클릭 이벤트 설정
            binding.imageViewPhoto.setOnClickListener {
                listener?.onPhotoClick(photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val height = viewModel.getPhotoHeight(position)

        // dp를 px로 변환
        val scale = holder.itemView.context.resources.displayMetrics.density
        val heightInPx = (height * scale + 0.5f).toInt()


        holder.bind(photoList[position], heightInPx)
    }

    override fun getItemCount(): Int = photoList.size

    fun setOnPhotoClickListener(listener: OnPhotoClickListener) {
        this.listener = listener
    }

    fun clearData() {
        photoList = listOf()
        notifyDataSetChanged()
    }


}