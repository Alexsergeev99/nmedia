package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Post

data class FeedModel(
    var posts: List<Post> = emptyList(),
    var loading: Boolean = false,
    var error: Boolean = false,
    var empty: Boolean = false
)
