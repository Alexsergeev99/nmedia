package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import java.util.concurrent.TimeUnit

class PostRepositoryRoomImpl() : PostRepository {

    private companion object{
        const val BASE_URL = "http://10.0.2.2:9999/"
        private val jsonType = "application/json".toMediaType()
    }

    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val type = object : TypeToken<List<Post>>() {}

    override fun getAll(): List<Post> {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()
        val call = client.newCall(request)
        val response = call.execute()

        val bodyText = requireNotNull(response.body).string()

        return gson.fromJson(bodyText, type)
    }

    override fun save(post: Post) : Post {
        val request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .post(gson.toJson(post, Post::class.java).toRequestBody(jsonType))
            .build()
        val call = client.newCall(request)
        val response = call.execute()

        val bodyText = requireNotNull(response.body).string()

        return gson.fromJson(bodyText, Post::class.java)
    }

    override fun likeById(post: Post) : Post{
        val request = if(post.likedByMe) {
            Request.Builder()
                .delete()
                .url("${BASE_URL}/api/posts/${post.id}/likes")
                .build()

        } else {
            Request.Builder()
                .url("${BASE_URL}/api/posts/${post.id}/likes")
                .post(gson.toJson(post, Post::class.java).toRequestBody(jsonType))
                .build()
        }
        val call = client.newCall(request)
        val response = call.execute()

        val bodyText = requireNotNull(response.body).string()

        return gson.fromJson(bodyText, Post::class.java)
    }

    override fun removeById(id: Long) {
        val request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun shareById(id: Long) = TODO()
}