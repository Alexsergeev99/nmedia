package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun Int.toShortString(): String = when (this) {
            in 0..<1_000 -> this.toString()
            in 1_000..<10_000 -> "${ (this / 100) / 10.0 }K"
            in 10_000..<1_000_000 -> "${ this / 1000 }K"
            in 1_000_000..<10_000_000 -> "${(this / 100_000) / 10.0}M"
            in 10_000_000..<1_000_000_000 -> "${this / 1_000_000}M"
            else -> "MANY"
        }

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                data.text = post.published
                mainText.text = post.content
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.liked else R.drawable.likes
                )
                binding.likesCounter.text = post.likes.toShortString()
                binding.repostCounter.text = post.shares.toShortString()
            }

            binding.likes.setOnClickListener {
                viewModel.like()
            }
            binding.reposts.setOnClickListener {
                viewModel.share()
            }
        }
    }
}