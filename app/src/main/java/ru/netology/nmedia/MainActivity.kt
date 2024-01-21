package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 0,
            shares = 0
        )

        fun Int.toShortString(): String = when (this) {
            in 0..<1_000 -> this.toString()
            in 1_000..<10_000 -> "${ (this / 100) / 10.0 }K"
            in 10_000..<1_000_000 -> "${ this / 1000 }K"
            in 1_000_000..<10_000_000 -> "${(this / 100_000) / 10.0}M"
            in 10_000_000..<1_000_000_000 -> "${this / 1_000_000}M"
            else -> "MANY"
        }

        with(binding) {
            author.text = post.author
            data.text = post.published
            mainText.text = post.content
        if(post.likedByMe) {
            likes.setImageResource(R.drawable.liked)
        }

            likes.setOnClickListener {
                post.likedByMe = !post.likedByMe

                likes.setImageResource(
                    if (post.likedByMe) R.drawable.liked else R.drawable.likes
                )
                if (post.likedByMe) binding.likesCounter.text = (++post.likes).toShortString()
                else binding.likesCounter.text = (--post.likes).toShortString()

            }
            reposts.setOnClickListener {
                binding.repostCounter.text = (++post.shares).toShortString()
            }
        }
    }
}