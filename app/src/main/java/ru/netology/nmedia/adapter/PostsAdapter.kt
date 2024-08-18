package ru.netology.nmedia.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.databinding.SeparatorBinding
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Separator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface onInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onClick(post: Post)
    fun onClickPhoto(post: Post)
}


class PostsAdapter(private val onInteractionListener: onInteractionListener) :
    PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(PostDifCallBack) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Separator -> R.layout.separator
            is Post -> R.layout.card_post
            else -> error("error")
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.card_post -> {
                val binding =
                    CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, onInteractionListener)
            }

            R.layout.separator -> {
                val binding =
                    SeparatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SeparatorViewHolder(binding)
            }

            else -> error("error")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Separator -> (holder as? SeparatorViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.bind(item)
            else -> error("error")
        }
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: onInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun Int.toShortString(): String = when (this) {
        in 0..<1_000 -> this.toString()
        in 1_000..<10_000 -> "${(this / 100) / 10.0}K"
        in 10_000..<1_000_000 -> "${this / 1000}K"
        in 1_000_000..<10_000_000 -> "${(this / 100_000) / 10.0}M"
        in 10_000_000..<1_000_000_000 -> "${this / 1_000_000}M"
        else -> "MANY"
    }

    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            data.text = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
                .format(Date((post.published).toLong() * 1000))
            mainText.text = post.content
            likes.isChecked = post.likedByMe
            likes.text = post.likes.toShortString()

            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            reposts.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            if (post.video != null) {
                binding.videoPicture.visibility = View.VISIBLE
                binding.playButton.visibility = View.VISIBLE
            }

            videoPicture.setOnClickListener {
                videoPicture.context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(post.video)
                    )
                )
            }

            playButton.setOnClickListener {
                playButton.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(post.video)))
            }

            root.setOnClickListener {
                onInteractionListener.onClick(post)
            }
            attachment.setOnClickListener {
                onInteractionListener.onClickPhoto(post)
            }

            Glide.with(binding.avatar)
                .load("$BASE_URL/avatars/${post.authorAvatar}")
                .circleCrop()
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .timeout(30000)
                .into(binding.avatar)

            if (post.attachment != null) {
                binding.attachment.isVisible = true
                Glide.with(binding.attachment)
                    .load("$BASE_URL/media/${post.attachment?.url}")
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_error)
                    .timeout(30000)
                    .into(binding.attachment)
            } else {
                binding.attachment.isVisible = false
            }


            menu.isVisible = post.ownedByMe

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
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

class SeparatorViewHolder(
    private val binding: SeparatorBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(separator: Separator) {
        binding.separatorText.text = "Сегодня"
    }

}

object PostDifCallBack : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem) = oldItem == newItem

}
