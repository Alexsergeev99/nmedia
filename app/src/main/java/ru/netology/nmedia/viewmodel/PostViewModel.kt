package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl

private val empty: Post = Post(
    id = 0,
 author = "Нетология. Институт интернет профессий",
 content = "",
 published = "1 марта в 10:00",
 likes = 0,
 likedByMe = false,
 shares = 0,
    video = null
)
class PostViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryRoomImpl(
        AppDb.getInstance(application).postDao
    )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun Int.toShortString(): String = when (this) {
        in 0..<1_000 -> this.toString()
        in 1_000..<10_000 -> "${ (this / 100) / 10.0 }K"
        in 10_000..<1_000_000 -> "${ this / 1000 }K"
        in 1_000_000..<10_000_000 -> "${(this / 100_000) / 10.0}M"
        in 10_000_000..<1_000_000_000 -> "${this / 1_000_000}M"
        else -> "MANY"
    }

    fun edit(post: Post) {
        edited.value = post
        return
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            else {
                edited.value = it.copy(content = text)
            }
        }
    }
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
}