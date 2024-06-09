package ru.netology.nmedia.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.errors.ApiError
import ru.netology.nmedia.errors.AppError
import ru.netology.nmedia.errors.NetworkError
import ru.netology.nmedia.errors.UnknownError
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


class PostRepositoryRoomImpl @Inject constructor(
    private val dao: PostDao,
    private val apiService: ApiService
) : PostRepository {

//    override val data = dao.getAllNewer()
//        .map(List<PostEntity>::toDto)
//        .flowOn(Dispatchers.Default)

    override val data = Pager(
    config = PagingConfig(
    pageSize = 10,
    enablePlaceholders = false),
        pagingSourceFactory = {
            PostPagingSource(apiService)
        }
    ).flow

    override suspend fun getAll() {
        try {
            val response = apiService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long) {
        try {
            dao.likeById(id)
            val response = apiService.likeById(id)
            if (!response.isSuccessful) {
                dao.likeById(id)
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            dao.likeById(id)
            throw NetworkError
        } catch (e: Exception) {
            dao.likeById(id)
            throw UnknownError
        }
    }

    override suspend fun shareById(id: Long) = TODO()
    override suspend fun removeById(id: Long) {
        val removedPost = dao.getPostById(id)
        try {
            dao.removeById(id)
            val response = apiService.removeById(id)
            if (!response.isSuccessful) {
                dao.insert(removedPost)
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            dao.insert(removedPost)
            throw NetworkError
        } catch (e: Exception) {
            dao.insert(removedPost)
            throw UnknownError
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = apiService.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWithAttachments(post: Post, upload: MediaUpload) {
        try {
            val media = upload(upload)
            val postWithAttachment = post.copy(
                attachment = ru.netology.nmedia.entity.Attachment(
                    media.id,
                    "",
                    AttachmentType.IMAGE
                )
            )
            save(postWithAttachment)
        } catch (e: AppError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody()
            )
            val response = apiService.upload(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun uploadUser(login: String, password: String) {
        try {
            val response = apiService.uploadUser(login, password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override fun getNewerCount(newerId: Long): Flow<Int> = flow {
        while (true) {
            delay(10.seconds)
            try {
                val response = apiService.getNewer(newerId)
                val body = response.body() ?: continue
                dao.insertInBackground(body.toEntity(false))
                emit(body.size)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun showAll() {
        dao.showAll()
    }
}
