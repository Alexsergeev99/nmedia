package ru.netology.nmedia.auth

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.bumptech.glide.Glide.init
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.netology.nmedia.api.Api
import ru.netology.nmedia.dto.PushToken
import ru.netology.nmedia.dto.Token

class AppAuth private constructor(context: Context) {
    val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val _state = MutableStateFlow<Token?>(null)
    val state = _state.asStateFlow()

    init {
        val id = prefs.getLong(ID_KEY, 0L)
        val token = prefs.getString(TOKEN_KEY, null)

        if(id != 0L && token != null) {
            _state.value = Token(id,  token)
        } else {
            prefs.edit { clear() }
        }
        sendPushToken()
        Log.d("eee", token.toString())
    }

    fun sendPushToken(token: String? = null) {
        try {
            CoroutineScope(Dispatchers.Default).launch {
                val push = PushToken(token ?: Firebase.messaging.token.await())
                Api.retrofitService.pushToken(push)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setAuth(id: Long, token: String) {
        _state.value = Token(id, token)
        prefs.edit {
            putLong(ID_KEY, id)
            putString(TOKEN_KEY, token)
        }
        sendPushToken()
    }

    fun clearAuth() {
        _state.value = null
        prefs.edit { clear() }
        sendPushToken()
    }

    companion object {
        private const val ID_KEY = "ID_KEY"
        private const val TOKEN_KEY = "TOKEN_KEY"

        private var INSTANCE:AppAuth? = null

        fun getInstance() = requireNotNull(INSTANCE) {

        }

        fun init(context: Context) {
            INSTANCE = AppAuth(context.applicationContext)
        }
    }
}