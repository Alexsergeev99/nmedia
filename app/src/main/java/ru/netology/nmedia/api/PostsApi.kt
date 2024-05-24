package ru.netology.nmedia.api

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Token
import ru.netology.nmedia.dto.User
import java.util.concurrent.TimeUnit

val BASE_URL = "http://10.0.2.2:9999/api/slow/"

val retrofit = Retrofit.Builder()
    .client(
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .run {
                if (BuildConfig.DEBUG) {
                    addInterceptor { chain ->
                        chain.proceed(chain.run {
                            val token = AppAuth.getInstance().state.value?.token

                            if (token != null) {
                                request().newBuilder()
                                    .addHeader("Autorization", token)
                                    .build()
                            } else {
                                request()
                            }
                        }
                        )
                    }.build()
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                } else {
                    this
                }
            }
            .build()
    )
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface PostsApiService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @Multipart
    @POST("media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun uploadUser(@Field("login") login: String, @Field("pass") pass: String): Response<Token>
}

object PostsApi {
    val retrofitService: PostsApiService by lazy {
        retrofit.create()
    }
}