package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: Flow<PagingData<Post>>
    suspend fun getAll()
    suspend fun getLatest()

    suspend fun likeById(id: Long)
    suspend fun shareById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(post: Post)
    suspend fun saveWithAttachments(post: Post, upload: MediaUpload)
    fun getNewerCount(newerId: Long): Flow<Int>
    suspend fun showAll()
    suspend fun upload(upload: MediaUpload): Media
    suspend fun uploadUser(login: String, password: String)
}