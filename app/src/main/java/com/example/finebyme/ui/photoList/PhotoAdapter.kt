package com.example.finebyme.ui.photoList

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finebyme.data.db.Photo
import com.example.finebyme.databinding.ItemPhotoBinding
import com.example.finebyme.ui.base.BaseViewModel
import com.example.finebyme.utils.ImageLoader
import com.example.finebyme.utils.PhotoDiffCallback

class PhotoAdapter(private val viewModel: BaseViewModel) :
    ListAdapter<Photo, PhotoAdapter.PhotoViewHolder>(diffUtil) {

//    private var photoList: List<Photo> = listOf()
    private var listener: OnPhotoClickListener? = null


//    fun setPhoto(newPhotoList: List<Photo>) {
//        val diffCallback = PhotoDiffCallback(photoList, newPhotoList)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        photoList = newPhotoList
//        diffResult.dispatchUpdatesTo(this)
//    }

    interface OnPhotoClickListener {
        fun onPhotoClick(photo: Photo)
    }

    class PhotoViewHolder(
        private val binding: ItemPhotoBinding,
        private val listener: OnPhotoClickListener?
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


        holder.bind(getItem(position), heightInPx)
    }

//    override fun getItemCount(): Int = photoList.size

    fun setOnPhotoClickListener(listener: OnPhotoClickListener) {
        this.listener = listener
    }

    fun clearData() {
//        val diffCallback = PhotoDiffCallback(photoList, listOf())
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        photoList = listOf()
//        diffResult.dispatchUpdatesTo(this)
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