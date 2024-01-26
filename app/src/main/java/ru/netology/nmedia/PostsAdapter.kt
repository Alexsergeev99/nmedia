package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

typealias onLikeListener = (Post) -> Unit
typealias  onShareListener = (Post) -> Unit
class PostsAdapter(private val onLike : onLikeListener,
                   private  val onShare : onShareListener)
    : ListAdapter<Post, PostViewHolder>(PostDifCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
   val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return PostViewHolder(binding, onLike, onShare)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
    holder.bind(getItem(position))
    }

}

class PostViewHolder (
private  val binding: CardPostBinding,
private val onLike: onLikeListener,
private  val onShare: onShareListener
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
            likes.setImageResource(
                if (post.likedByMe) R.drawable.liked else R.drawable.likes
            )
            binding.repostCounter.text = post.shares.toShortString()
            binding.likesCounter.text = post.likes.toShortString()

            likes.setOnClickListener {
                onLike(post)
            }
            reposts.setOnClickListener {
                onShare(post)
            }
        }
    }
    }

object  PostDifCallBack: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}
