package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryRoomImpl(AppDb.getInstance(context = application).postDao)

//    private val _data = MutableLiveData(-1)
//    val data: LiveData<Int>
//        get() = _data
//
//    suspend fun uploadUser(login: String, password: String) {
//        viewModelScope.launch {
//            val token: Token
//            try {
//                val response = PostsApi.retrofitService.uploadUser(login, password)
//
//                if (!response.isSuccessful) {
//                    _data.value = 1
////                    throw ApiError(response.code(), response.message())
//                } else {
//                    token = (response.body() ?: Token(id = 0, token = "")) as Token
//                    AppAuth.getInstance().setAuth(token.id, token.token)
//                    _data.value = 0
//                }
//            } catch (e: IOException) {
//                _data.value = 2
//
////                throw NetworkError
//            } catch (e: Exception) {
//                _data.value = 3
//
////                throw UnknownError
//            }
//    }
//}
    }