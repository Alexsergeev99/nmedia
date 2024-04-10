package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(callback: GetAllCallback)
    fun likeById(post: Post, callback: LikeByIdCallback)
    fun shareById(id: Long)
    fun removeById(id: Long, callback: RemoveByIdCallback)
    fun save(post: Post, callback: SaveCallback)

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
        fun onSuccess()
        fun onError(e: Exception)
    }
}