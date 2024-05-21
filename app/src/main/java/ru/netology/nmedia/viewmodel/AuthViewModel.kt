package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import ru.netology.nmedia.auth.AppAuth

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    val auth = AppAuth.getInstance().state.asLiveData()
    val authorized: Boolean
        get() = auth.value?.token != null
}