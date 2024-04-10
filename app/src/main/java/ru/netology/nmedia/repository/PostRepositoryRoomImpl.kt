package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryRoomImpl() : PostRepository {

    private companion object{
        const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

//    private var posts = emptyList<Post>()
//    private val data = MutableLiveData(posts)
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    override fun getAll(callback: PostRepository.GetAllCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .enqueue(
                object  : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }
                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        try {
                            callback.onSuccess(gson.fromJson(responseBody, typeToken.type))
                        } catch (e:Exception) {
                            callback.onError(e)
                        }
                    }
                }
            )
    }
    override fun save(post: Post, callback: PostRepository.SaveCallback) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(
                object  : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }
                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        try {
                            callback.onSuccess(gson.fromJson(responseBody, Post::class.java))
                        } catch (e:Exception) {
                            callback.onError(e)
                        }
                    }
                }
            )
    }

    override fun likeById(post: Post, callback: PostRepository.LikeByIdCallback) {
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

        client.newCall(request)
            .enqueue(
                object  : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        try {
                            callback.onSuccess(gson.fromJson(responseBody, Post::class.java))
                        } catch (e:Exception) {
                            callback.onError(e)
                        }
                    }

                }
            )
    }

    override fun removeById(id: Long, callback: PostRepository.RemoveByIdCallback) {
        val request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/${id}")
            .build()

        client.newCall(request)
            .enqueue(
                object  : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }
                    override fun onResponse(call: Call, response: Response) {
//                        val responseBody = response.body?.string()
                        try {
                            callback.onSuccess()
                        } catch (e:Exception) {
                            callback.onError(e)
                        }
                    }
                }
            )
    }

    override fun shareById(id: Long) = TODO()
}