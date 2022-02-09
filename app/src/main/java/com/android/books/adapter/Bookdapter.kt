package com.android.books.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.books.databinding.ItemBookBinding
import com.android.books.network.VolumeInfo
import com.bumptech.glide.Glide

class Bookdapter(val list: MutableList<VolumeInfo>) :
    RecyclerView.Adapter<Bookdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    lateinit var onItemClick: ((VolumeInfo) -> Unit)


    inner class BookViewHolder(val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: VolumeInfo) {
//            itemView.setOnClickListener {
//                onItemClick.invoke(book)
//            }
            book.volumeInfo.imageLinks?.smallThumbnail?.let {
                Glide.with(itemView).load(it)
                    .circleCrop()
                    .into(binding.imgBook)
            }

            binding.itemDescription.text = book.volumeInfo.description ?: "Sem descrição"
            binding.itemPubliser.text = book.volumeInfo.publishedDate ?: "Sem data"
            binding.itemTitle.text = book.volumeInfo.description ?: "Sem titulo"
        }

    }
}