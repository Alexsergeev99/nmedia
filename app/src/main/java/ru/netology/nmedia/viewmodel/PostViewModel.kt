package ru.netology.nmedia.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
 author = "Нетология. Институт интернет профессий",
 content = "",
 published = "",
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
            _data.value = FeedModel(loading = true)

                repository.getAll(object  : PostRepository.GetAllCallback {
                 override fun onSuccess(posts: List<Post>) {
                     _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
                 }
                    override fun onError(e: Exception) {
                        _data.value = FeedModel(error = true)
                    }
                })
    }
    fun save() {
        edited.value?.let {
                repository.save(it, object  : PostRepository.SaveCallback {
                    override fun onSuccess(post: Post) {
                        _postCreated.postValue(Unit)
                        edited.value = empty
                    }
                    override fun onError(e: Exception) {
//                        edited.value = empty
                    }
                })
        }
    }

    fun edit(post: Post) {
        edited.value = post
        return
    }

    fun changeContent(content: String) {
    val text = content.trim()
    if (edited.value?.content == text) {
        return
    }
    edited.value = edited.value?.copy(content = text)
}
    fun likeById(id: Long) {
        val post = _data.value?.posts?.find { it.id == id }?: empty
        repository.likeById(post, object : PostRepository.LikeByIdCallback {
            override fun onSuccess(post: Post) {
                _data.value = _data.value?.copy(
                    posts = _data.value?.posts.orEmpty().map {
                    if (it.id == id) post else it
                }
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        }
            )
    }
    fun shareById(id: Long) = repository.shareById(id)

    fun removeById(id: Long) {
        repository.removeById(id, object : PostRepository.RemoveByIdCallback {
            override fun onSuccess(Unit: Unit) {
                _data.value = FeedModel(posts = _data.value?.posts.orEmpty().filter {
                    it.id != id }
                )
            }
            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
            }
        }
        )
    }

}