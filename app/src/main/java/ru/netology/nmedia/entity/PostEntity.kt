//package ru.netology.nmedia.entity
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import ru.netology.nmedia.dto.Attachment
//import ru.netology.nmedia.dto.Post
//import ru.netology.nmedia.enumeration.AttachmentType
//
//@Entity
//data class PostEntity (
//    @PrimaryKey(autoGenerate = true)
//    val id: Long,
//    val author: String,
//    val authorAvatar: String = "",
//    val content: String,
//    val published: String,
//    val likes: Int = 0,
//    val likedByMe: Boolean = false,
//    val shares: Int = 0,
//    val video: String? = null,
//    var attachment: Attachment? = null,
//    ) {
//    fun toDto() = Post(id, author, authorAvatar, content, published, likes, likedByMe, shares, video, attachment)
//
//    companion object {
//    fun fromDto(post: Post) = PostEntity(
//        id = post.id,
//        author = post.author,
//        content = post.content,
//        published = post.published,
//        likes = post.likes,
//        likedByMe = post.likedByMe,
//        shares = post.shares,
//        video = post.video
//    )
//
//    }
//}
//
//data class Attachment (
//    val url: String,
//    val description: String?,
//    val type: AttachmentType,
//)