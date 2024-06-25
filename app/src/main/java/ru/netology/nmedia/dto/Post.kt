package ru.netology.nmedia.dto

import ru.netology.nmedia.entity.Attachment
import ru.netology.nmedia.enumeration.AttachmentType

sealed interface FeedItem{
    val id: Long
}

data class Post(
    override val id: Long,
    val author: String,
    val authorId: Long,
    val authorAvatar: String = "",
    val content: String,
    val published: String,
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    val shares: Int = 0,
    val video: String? = null,
    val visibility: Boolean = true,
    var attachment: Attachment? = null,
    val ownedByMe: Boolean = false
) : FeedItem

data class Separator(
    override val id: Long,
    val title: String
) : FeedItem

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachmentType,
)

data class PushToken(val token: String)