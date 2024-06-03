package ru.netology.nmedia.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.PhotoModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject

private val empty = Post(
    id = 0,
    author = "",
    authorId = 0,
    authorAvatar = "",
    content = "",
    published = "",
    likes = 0,
    likedByMe = false,
    shares = 0,
    video = null
)

@HiltViewModel
class PostViewModel @Inject constructor(private val repository: PostRepository,
    private val appAuth: AppAuth) : ViewModel() {

    @Inject
    lateinit var appDb: AppDb

    private val noPhoto = PhotoModel()
//    private val repository: PostRepository =
//        PostRepositoryRoomImpl(appDb(context = application).postDao)
    private val _data = MutableLiveData(FeedModel())
    @OptIn(ExperimentalCoroutinesApi::class)
    val data: LiveData<FeedModel> = appAuth
        .state
        .flatMapLatest { auth ->
            repository.data
                .map { posts ->
                    FeedModel(
                        posts.map { it.copy(ownedByMe = it.authorId == auth?.id) },
//                        posts.isEmpty()
                    )
                }
        }.asLiveData(Dispatchers.Default)
    val edited = MutableLiveData(empty)
    private val _dataState = MutableLiveData(FeedModelState())
    val dataState: LiveData<FeedModelState>
        get() = _dataState
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    val _errorMessage = SingleLiveEvent<Unit>()
    val errorMessage: LiveData<Unit>
        get() = _errorMessage
    private val _photo = MutableLiveData<PhotoModel>(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    val newerCount: LiveData<Int> = data.switchMap {
        val newerId = it.posts.firstOrNull()?.id ?: 0L
        repository.getNewerCount(newerId).asLiveData(Dispatchers.Default)
    }

    fun Int.toShortString(): String = when (this) {
        in 0..<1_000 -> this.toString()
        in 1_000..<10_000 -> "${(this / 100) / 10.0}K"
        in 10_000..<1_000_000 -> "${this / 1000}K"
        in 1_000_000..<10_000_000 -> "${(this / 100_000) / 10.0}M"
        in 10_000_000..<1_000_000_000 -> "${this / 1_000_000}M"
        else -> "MANY"
    }

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            repository.showAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun save() {
        viewModelScope.launch {
            try {
                edited.value?.let {
                    repository.save(it)
                    _postCreated.postValue(Unit)
                    edited.value = empty
                }
            } catch (e: Exception) {
                _errorMessage.value = Unit
            }
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
        viewModelScope.launch {
            try {
                val post = _data.value?.posts?.find { it.id == id } ?: empty
                repository.likeById(id)
                _data.value = _data.value?.copy(
                    posts = _data.value?.posts.orEmpty().map {
                        if (it.id == id) post else it
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = Unit
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun shareById(id: Long) = viewModelScope.launch {
        repository.shareById(id)
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removeById(id)
                _data.value = FeedModel(posts = _data.value?.posts.orEmpty().filter {
                    it.id != id
                })
            } catch (e: Exception) {
                _errorMessage.value = Unit
                _dataState.value = FeedModelState(error = true)
            }

        }
    }

    fun showNewPosts() = viewModelScope.launch {
        try {
            repository.showAll()
        } catch (e: Exception) {
            throw e
        }
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }
}