package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.concurrent.thread

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
    private val repository: PostRepository = PostRepositoryRoomImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)

            }
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

    init {
        load()
    }

    fun load() {
        thread {
            _data.postValue(FeedModel(loading = true))

            val  model = try {
                val posts = repository.getAll()

                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e:Exception) {
                FeedModel(error = true)
            }
            _data.postValue(model)
        }
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
    fun likeById(post: Post): Post {
        thread {
            if (post.likedByMe) {
                repository.likeById(post)
                post.likedByMe != post.likedByMe
                post.likes--
            } else {
                repository.likeById(post)
                post.likedByMe != post.likedByMe
                post.likes++
            }
        }
        return  post
    }
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) {
        thread {
            _data.postValue(_data.value?.copy
                (posts = _data.value?.posts.orEmpty().filter {
                it.id != id
            })
            )
            repository.removeById(id)
        }
    }
}