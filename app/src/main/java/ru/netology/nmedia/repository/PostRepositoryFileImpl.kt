package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFileImpl(private val context: Context) : PostRepository {
    var nextId: Long = 0

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var posts = emptyList<Post>()
        private  set(value) {
            field = value
            data.value = value
            sync()
        }
    private val fileName = "posts.json"
    private val data = MutableLiveData(posts)

    init {
        if (context.filesDir.resolve(fileName).exists()) {
            context.openFileInput(fileName).bufferedReader().use {
                posts = gson.fromJson(it, type)
                nextId = posts.maxOfOrNull { it.id }?.inc() ?: 1
            }
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likes = if (it.likedByMe) --it.likes else ++it.likes,
                likedByMe = !it.likedByMe
            )
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Нетология. Университет интернет-профессий будущего",
                    published = "now",
                    likedByMe = false,
                    likes = 0,
                    shares = 0,
                    video = null
                )
            ) + posts
        } else {
            posts = posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
        return
    }

    private fun sync(){
        context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
}

