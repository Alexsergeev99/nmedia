package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(post: Post): Post
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)

    interface GetAllCallback{
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

    interface SaveCallback{
        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }

    interface LikeByIdCallback{
        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }

    interface RemoveByIdCallback{
        fun onSuccess(id: Long)
        fun onError(e: Exception)
    }
}