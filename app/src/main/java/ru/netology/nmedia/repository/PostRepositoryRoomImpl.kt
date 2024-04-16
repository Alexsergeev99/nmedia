package ru.netology.nmedia.repository

import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post

class PostRepositoryRoomImpl() : PostRepository {

    override fun getAll(callback: PostRepository.GetAllCallback) {
       PostsApi.retrofitService.getAll()
            .enqueue(
                object  : retrofit2.Callback<List<Post>> {

                    override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
                        callback.onError(Exception(t))
                    }

                    override fun onResponse(
                        call: retrofit2.Call<List<Post>>,
                        response: retrofit2.Response<List<Post>>
                    ) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                            return
                        }
                        val responseBody = response.body() ?: throw Exception("body is null")
                        callback.onSuccess(responseBody)
                    }
                }
            )
    }
    override fun save(post: Post, callback: PostRepository.SaveCallback) {
       PostsApi.retrofitService.save(post)
            .enqueue(
                object  : retrofit2.Callback<Post> {

                    override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                        callback.onError(Exception(t))
                    }

                    override fun onResponse(
                        call: retrofit2.Call<Post>,
                        response: retrofit2.Response<Post>
                    ) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                            return
                        }
                        val responseBody = response.body() ?: throw Exception("body is null")
                        callback.onSuccess(responseBody)
                    }

                }
            )
    }

    override fun likeById(post: Post, callback: PostRepository.LikeByIdCallback) {
        if (post.likedByMe) {
            PostsApi.retrofitService.unlikeById(post.id)
                .enqueue(
                    object : retrofit2.Callback<Post> {

                        override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                            callback.onError(Exception(t))
                        }

                        override fun onResponse(
                            call: retrofit2.Call<Post>,
                            response: retrofit2.Response<Post>
                        ) {
                            if (!response.isSuccessful) {
                                callback.onError(RuntimeException(response.message()))
                                return
                            }
                            val responseBody = response.body() ?: throw Exception("body is null")
                            callback.onSuccess(responseBody)
                        }
                    }
                )
        } else {
            PostsApi.retrofitService.likeById(post.id)
                .enqueue(
                    object : retrofit2.Callback<Post> {

                        override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                            callback.onError(Exception(t))
                        }

                        override fun onResponse(
                            call: retrofit2.Call<Post>,
                            response: retrofit2.Response<Post>
                        ) {
                            if (!response.isSuccessful) {
                                callback.onError(RuntimeException(response.message()))
                                return
                            }
                            val responseBody = response.body() ?: throw Exception("body is null")
                            callback.onSuccess(responseBody)
                        }
                    }
                )
        }
    }

    override fun removeById(id: Long, callback: PostRepository.RemoveByIdCallback) {
        PostsApi.retrofitService
            .removeById(id)
            .enqueue(
                object  : retrofit2.Callback<Unit> {

                    override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                        callback.onError(Exception(t))
                    }

                    override fun onResponse(
                        call: retrofit2.Call<Unit>,
                        response: retrofit2.Response<Unit>
                    ) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                            return
                        }
                        callback.onSuccess(Unit)
                    }
                }
            )
    }

    override fun shareById(id: Long) = TODO()
}