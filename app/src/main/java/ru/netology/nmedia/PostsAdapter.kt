package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

interface onInteractionListener{
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
}


class PostsAdapter(private val onInteractionListener: onInteractionListener)
    : ListAdapter<Post, PostViewHolder>(PostDifCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
   val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
    holder.bind(getItem(position))
    }

}

class PostViewHolder (
private  val binding: CardPostBinding,
private val onInteractionListener: onInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun Int.toShortString(): String = when (this) {
        in 0..<1_000 -> this.toString()
        in 1_000..<10_000 -> "${ (this / 100) / 10.0 }K"
        in 10_000..<1_000_000 -> "${ this / 1000 }K"
        in 1_000_000..<10_000_000 -> "${(this / 100_000) / 10.0}M"
        in 10_000_000..<1_000_000_000 -> "${this / 1_000_000}M"
        else -> "MANY"
    }
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            data.text = post.published
            mainText.text = post.content
            likes.isChecked = post.likedByMe
            binding.reposts.text = post.shares.toShortString()
           likes.text = post.likes.toShortString()

            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            reposts.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener {item ->
                     when(item.itemId) {
                         R.id.remove -> {
                             onInteractionListener.onRemove(post)
                             true
                         }
                         R.id.edit -> {
                             onInteractionListener.onEdit(post)
                             true
                         }
                         else ->
                             false
                     }
                    }
                }.show()
            }
        }
    }
    }
object  PostDifCallBack: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}
