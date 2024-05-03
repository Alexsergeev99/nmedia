package ru.netology.nmedia.dto

import ru.netology.nmedia.entity.Attachment
import ru.netology.nmedia.enumeration.AttachmentType

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String = "",
    val content: String,
    val published: String,
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    val shares: Int = 0,
    val video: String? = null,
    var attachment: Attachment? = null,
    )

data class Attachment (
    val url: String,
    val description: String?,
    val type: AttachmentType,
)

//data class Post(
//    val id: Long,
//    val author: String,
//    val authorAvatar: String,
//    val content: String,
//    val published: String,
//    val likedByMe: Boolean,
//    val likes: Int = 0,
//)