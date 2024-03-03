package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    var shares: Int = 0,
    var video: String? = null
) {
    fun toDto() = Post(id, author = "Нетология. Институт интернет профессий", content, published = "1 марта в 10:00", likes, likedByMe, shares, video)

    companion object {
    fun fromDto(post: Post) = PostEntity(
        id = post.id,
        author = post.author,
        content = post.content,
        published = post.published,
        likes = post.likes,
        likedByMe = post.likedByMe,
        shares = post.shares,
        video = post.video
    )

    }
}