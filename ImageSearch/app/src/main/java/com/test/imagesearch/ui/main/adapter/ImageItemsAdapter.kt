package com.test.imagesearch.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.imagesearch.data.database.Thumbnail
import com.test.imagesearch.databinding.ImageItemBinding

class ImageItemsAdapter(
    private var items: ArrayList<Thumbnail>,
    private val itemClickListener: (thumbnail: Thumbnail) -> Unit
) :
    RecyclerView.Adapter<ImageItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImageItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Thumbnail) {
            binding.imageUrl = item.imageUrl
            binding.imageItem.setOnClickListener {
                itemClickListener.invoke(item)
            }
            binding.executePendingBindings()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String) {
            Glide.with(view.context).load(url)
                .into(view)
        }
    }

    fun updateDataSet(newItems: ArrayList<Thumbnail>) {
        items.clear()
        items.addAll(newItems)
        this.notifyDataSetChanged()
    }
}
