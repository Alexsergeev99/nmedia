package ru.netology.nmedia.dto

import android.net.Uri

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    val shares: Int = 0,
    val video: String? = null
    )

