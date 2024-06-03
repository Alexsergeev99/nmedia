package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Token
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: AppAuth,
    private val apiService: ApiService
) : ViewModel() {
    val authData = auth.state.asLiveData()
    val authorized: Boolean
        get() = authData.value?.token != null
    private val _data = MutableLiveData(-1)
    val data: LiveData<Int>
        get() = _data

    suspend fun uploadUser(login: String, password: String) {
        viewModelScope.launch {
            val token: Token
            try {
                val response = apiService.uploadUser(login, password)

                if (!response.isSuccessful) {
                    _data.value = 1
                } else {
                    token = response.body() ?: Token(id = 0, token = "")
                    auth.setAuth(token.id, token.token)
                    _data.value = 0
                }
            } catch (e: IOException) {
                _data.value = 2
            } catch (e: Exception) {
                _data.value = 3
            }
        }
    }
}