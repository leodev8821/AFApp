package com.example.afapp.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.afapp.database.Post
import com.example.afapp.database.User
import com.example.afapp.databinding.ItemPostBinding
import com.squareup.picasso.Picasso

class PostAdapter(
    private var items:List<Post> = listOf(),
    val onClickListener:(position:Int) -> Unit,

): RecyclerView.Adapter<PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder( holder: PostViewHolder, position: Int) {
        holder.render(items[position])
        holder.itemView.setOnClickListener {
            onClickListener(position)
        }
    }

    fun updateItems(results: List<Post>?) {
        items = results!!
        notifyDataSetChanged()
    }
}

class PostViewHolder(
    val binding: ItemPostBinding
):RecyclerView.ViewHolder(binding.root){

    fun render(
        post:Post
    ){
        val dateFormat = DateFormat.format("dd-MMMM-yyyy", post.date)

        binding.titleItemTextView.text = post.title
        binding.bodyItemTextView.text = post.body
        binding.tagsItemTextView.text = post.tags
        binding.userItemTextView.text = post.userPost.toString()
        binding.reactionsItemTextView.text = post.reactions.toString()
        binding.dateItemtextView.text = dateFormat
    }

}