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
            likedByMe = false
        )
        var likedCounter = 0
        var shares = 0

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
                if (post.likedByMe) likesCounter.text = likedCounter++.toString()
                else likesCounter.text = likedCounter--.toString()
                when (likedCounter) {
                    in 0..999 -> likesCounter.text = likedCounter.toString()
                    in 1000..1099,  in 2000..2099,  in 3000..3099,  in 4000..4099,
                    in 5000..5099,  in 6000..6099,  in 7000..7099,  in 8000..8099,
                    in 9000..9099-> likesCounter.text = "${likedCounter.toString().first()}K"
                    in 1100..1199,  in 2100..2199,  in 3100..3199,  in 4100..4199,
                    in 5100..5199,  in 6100..6199,  in 7100..7199,  in 8100..8199,
                    in 9100..9199-> likesCounter.text = "${likedCounter.toString().first()}.1K"
                    in 1200..1299,  in 2200..2299,  in 3200..3299,  in 4200..4299,
                    in 5200..5299,  in 6200..6299,  in 7200..7299,  in 8200..8299,
                    in 9200..9299-> likesCounter.text = "${likedCounter.toString().first()}.2K"
                    in 1300..1399,  in 2300..2399,  in 3300..3399,  in 4300..4399,
                    in 5300..5399,  in 6300..6399,  in 7300..7399,  in 8300..8399,
                    in 9300..9399-> likesCounter.text = "${likedCounter.toString().first()}.3K"
                    in 1400..1499,  in 2400..2499,  in 3400..3499,  in 4400..4499,
                    in 5400..5499,  in 6400..6499,  in 7400..7499,  in 8400..8499,
                    in 9400..9499-> likesCounter.text = "${likedCounter.toString().first()}.4K"
                    in 1500..1599,  in 2500..2599,  in 3500..3599,  in 4500..4599,
                    in 5500..5599,  in 6500..6599,  in 7500..7599,  in 8500..8599,
                    in 9500..9599-> likesCounter.text = "${likedCounter.toString().first()}.5K"
                    in 1600..1699,  in 2600..2699,  in 3600..3699,  in 4600..4699,
                    in 5600..5699,  in 6600..6699,  in 7600..7699,  in 8600..8699,
                    in 9600..9699-> likesCounter.text = "${likedCounter.toString().first()}.6K"
                    in 1700..1799,  in 2700..2799,  in 3700..3799,  in 4700..4799,
                    in 5700..5799,  in 6700..6799,  in 7700..7799,  in 8700..8799,
                    in 9700..9799-> likesCounter.text = "${likedCounter.toString().first()}.7K"
                    in 1800..1899,  in 2800..2899,  in 3800..3899,  in 4800..4899,
                    in 5800..5899,  in 6800..6899,  in 7800..7899,  in 8800..8899,
                    in 9800..9899-> likesCounter.text = "${likedCounter.toString().first()}.8K"
                    in 1900..1999,  in 2900..2999,  in 3900..3999,  in 4900..4999,
                    in 5900..5999,  in 6900..6999,  in 7900..7999,  in 8900..8999,
                    in 9900..9999-> likesCounter.text = "${likedCounter.toString().first()}.9K"
                    in 10000..999999 -> likesCounter.text = "${likedCounter/1000}K"
                    else -> likesCounter.text = "${likedCounter/100000}M"
                }
            }
            reposts.setOnClickListener {
                shares++
               when (shares) {
                   in 0..999 -> repostCounter.text = shares.toString()
                   in 1000..1099,  in 2000..2099,  in 3000..3099,  in 4000..4099,
                   in 5000..5099,  in 6000..6099,  in 7000..7099,  in 8000..8099,
                   in 9000..9099-> repostCounter.text = "${shares.toString().first()}K"
                   in 1100..1199,  in 2100..2199,  in 3100..3199,  in 4100..4199,
                   in 5100..5199,  in 6100..6199,  in 7100..7199,  in 8100..8199,
                   in 9100..9199-> repostCounter.text = "${shares.toString().first()}.1K"
                   in 1200..1299,  in 2200..2299,  in 3200..3299,  in 4200..4299,
                   in 5200..5299,  in 6200..6299,  in 7200..7299,  in 8200..8299,
                   in 9200..9299-> repostCounter.text = "${shares.toString().first()}.2K"
                   in 1300..1399,  in 2300..2399,  in 3300..3399,  in 4300..4399,
                   in 5300..5399,  in 6300..6399,  in 7300..7399,  in 8300..8399,
                   in 9300..9399-> repostCounter.text = "${shares.toString().first()}.3K"
                   in 1400..1499,  in 2400..2499,  in 3400..3499,  in 4400..4499,
                   in 5400..5499,  in 6400..6499,  in 7400..7499,  in 8400..8499,
                   in 9400..9499-> repostCounter.text = "${shares.toString().first()}.4K"
                   in 1500..1599,  in 2500..2599,  in 3500..3599,  in 4500..4599,
                   in 5500..5599,  in 6500..6599,  in 7500..7599,  in 8500..8599,
                   in 9500..9599-> repostCounter.text = "${shares.toString().first()}.5K"
                   in 1600..1699,  in 2600..2699,  in 3600..3699,  in 4600..4699,
                   in 5600..5699,  in 6600..6699,  in 7600..7699,  in 8600..8699,
                   in 9600..9699-> repostCounter.text = "${shares.toString().first()}.6K"
                   in 1700..1799,  in 2700..2799,  in 3700..3799,  in 4700..4799,
                   in 5700..5799,  in 6700..6799,  in 7700..7799,  in 8700..8799,
                   in 9700..9799-> repostCounter.text = "${shares.toString().first()}.7K"
                   in 1800..1899,  in 2800..2899,  in 3800..3899,  in 4800..4899,
                   in 5800..5899,  in 6800..6899,  in 7800..7899,  in 8800..8899,
                   in 9800..9899-> repostCounter.text = "${shares.toString().first()}.8K"
                   in 1900..1999,  in 2900..2999,  in 3900..3999,  in 4900..4999,
                   in 5900..5999,  in 6900..6999,  in 7900..7999,  in 8900..8999,
                   in 9900..9999-> repostCounter.text = "${shares.toString().first()}.9K"
                   in 10000..999999 -> repostCounter.text = "${shares/1000}K"
                   else -> repostCounter.text = "${shares/100000}M"
               }
            }
        }
    }
}