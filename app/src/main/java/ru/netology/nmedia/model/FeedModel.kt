package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Post

data class FeedModel(
    var posts: List<Post> = emptyList(),
    val empty: Boolean = false
)

data class FeedModelState(
    var loading: Boolean = false,
    var error: Boolean = false,
)
