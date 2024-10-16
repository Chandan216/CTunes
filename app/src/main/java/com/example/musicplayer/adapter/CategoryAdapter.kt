package com.example.musicplayer.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.SongsListActivity
import com.example.musicplayer.databinding.CategoryItemBinding
import com.example.musicplayer.models.CategoryModel

class CategoryAdapter(private val categoryList: List<CategoryModel>, private val isSinger: Boolean) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(category: CategoryModel, isSinger: Boolean) {
            binding.nameTextView.text = category.name

            val requestOptions = if (isSinger) {
                RequestOptions().circleCrop()
            } else {
                RequestOptions().transform(RoundedCorners(32))
            }

            Glide.with(binding.coverImageView).load(category.coverUrl)
                .apply(requestOptions)
                .into(binding.coverImageView)

            binding.root.setOnClickListener {
                val context = binding.root.context
                SongsListActivity.category = category
                context.startActivity(Intent(context, SongsListActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(categoryList[position], isSinger)
    }
}
