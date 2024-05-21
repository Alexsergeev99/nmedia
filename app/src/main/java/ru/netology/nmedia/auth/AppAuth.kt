package ru.netology.nmedia.auth

import android.content.Context
import androidx.core.content.edit
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    }

    fun setAuth(id: Long, token: String) {
        _state.value = Token(id, token)
        prefs.edit {
            putLong(ID_KEY, id)
            putString(TOKEN_KEY, token)
        }
    }

    fun clearAuth() {
        _state.value = null
        prefs.edit { clear() }
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